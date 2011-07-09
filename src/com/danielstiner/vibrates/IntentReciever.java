package com.danielstiner.vibrates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.Toast;

public class IntentReciever extends BroadcastReceiver {

	private static final String LOG_TAG = "CustomVibrates";

	private static final String VIB_STATE = "android.media.VIBRATE_SETTING_CHANGED";
	private static final String WIFI_STATE = "android.net.wifi.supplicant.STATE_CHANGE";
	private static final String PHONE_STATE = "android.intent.action.PHONE_STATE";
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String BATTERY_STATE = "android.intent.action.BATTERY_LOW";
	private static final String DATA_SMS_STATE = "android.intent.action.DATA_SMS_RECEIVED";
	private static final String HEADSET_BT_STATE = "android.bluetooth.headset.action.AUDIO_STATE_CHANGED";
	private static final String HEADSET_PLUG_STATE = "android.intent.action.HEADSET_PLUG";
	
	
	
	// Gmail constants
    private static final Uri CONVERSATIONS_URI = Uri.parse("content://gmail-ls/conversations/");

    private static final String[] CONVERSATION_PROJECTION = {
            "_id", "subject", "snippet", "fromAddress", "date"
    };
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Check that we have some extra info to actually work with
		Bundle bundle = intent.getExtras();
		
		//TODO temporary
		Toast.makeText(context, intent.getAction()+intent.getDataString(), Toast.LENGTH_SHORT).show();
		
		if(bundle == null) return;
		
		// Figure out what kind of intent we have
		String action = intent.getAction();
		if (action.equals(SMS_RECEIVED)) {
			// retrieve the SMS message(s)
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				handleSMS(SmsMessage.createFromPdu((byte[])pdus[i]), context);
			}
		} else if(action.equals(PHONE_STATE)) {
			handlePhoneState(bundle, context);
		} else if(action.equals(Intent.ACTION_PROVIDER_CHANGED)) {
			handleProviderChange(bundle, context).fire(context);
		}
		// TODO Others
	}

	private void handlePhoneState(Bundle bundle, Context context) {
		// Check if we are currently ringing
		if (bundle.getString(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			// Hand off handling of this event
			String number = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			new Notification(number, Notification.particularizeType(Notification.VOICE, "phone")).fire(context);
		}
	}

	private void handleSMS(SmsMessage msg, Context context) {

		// Hand off handling of this event
		// Start by creating a notifcation for the sender
		Notification n = new Notification(msg.getDisplayOriginatingAddress());

		if (msg.isEmail()) {
			// Must have been an SMS from an email address
			n.setType(Notification.particularizeType(Notification.SMS, "emailed"));
		} else {
			// Must have been a normal SMS
			n.setType(Notification.SMS);
		}
		
		// throw in the body of the message for good measure
		n.setExtra(msg.getDisplayMessageBody());

		// Will tell the vibrator service to get busy
		n.fire(context);
	}
	private Notification handleProviderChange(Bundle bundle, Context context) {
		
		//TODO temp
		if(bundle != null)
		return new Notification("");
		
		// Assume its a gmail for now
		String account = bundle.getString("account");
        String tagLabel = bundle.getString("tagLabel");
        
		// Get sender and subject
		
        Cursor cursor = context.getContentResolver().query(Uri.withAppendedPath(CONVERSATIONS_URI, account),
                null, "label:"+tagLabel, null, null);
        cursor.moveToFirst();
        String[] columns = cursor.getColumnNames(); //labelIds
        String fromAddress = cursor.getString(cursor.getColumnIndexOrThrow("fromAddress"));
        String subject = cursor.getString(cursor.getColumnIndexOrThrow("subject"));
        cursor.close();
        
        // TODO from address is actually wrong
        
        
        

        // Beware, dragons ahead.
        // Call Gmail code for formatting. This is really
        // hacky. Thanks to JesusFreke for baksmali.
        Class<?> gm;
        try {
            gm = Class.forName("android.provider.Gmail");
        } catch (Exception e) {
            Context foreignContext;
			try {
				foreignContext = context.createPackageContext("com.google.android.gm",
				        Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
				 try {
					gm = foreignContext.getClassLoader().loadClass(
					 "com.google.android.gm.provider.Gmail");
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return new Notification("");
				}
				 
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new Notification("");
			}
           
        }
        
        SpannableStringBuilder from = new SpannableStringBuilder(fromAddress);

        // Set up parameters
        String instructions = fromAddress;
        int maxChars = 0x19;
        StyleSpan unreadStyle = new StyleSpan(Typeface.BOLD);
        ForegroundColorSpan draftsStyle = new ForegroundColorSpan(Color.RED);
        String meString = "me"; // context.getString(R.string.me);
        String draftString = "draft"; // context.getString(R.string.draft);
        String draftPluralString = "drafts"; // context.getString(R.string.draftplural);
        String sendingString = "sending"; // context.getString(R.string.sending);
        String sendFailedString = "sendfailed"; // context.getString(R.string.sendingfailed);
        boolean forceAllUnread = false;
        boolean forceAllRead = false;

        boolean found = false;
        Method[] ms = gm.getMethods();
        for (int i = 0; i < ms.length; i++) {
            if (ms[i].getName().contains("getSenderSnippet")) {
                Class<?>[] params = ms[i].getParameterTypes();
                if (params.length == 13) {
                    // Gmail in Android 2.2 and up
                    try {
						ms[i].invoke(null, instructions, from, new SpannableStringBuilder(),
						        maxChars, unreadStyle, draftsStyle, meString, draftString,
						        draftPluralString, sendingString, sendFailedString, forceAllUnread,
						        forceAllRead);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } else {
                    // Earlier versions
                    try {
						ms[i].invoke(null, instructions, from, maxChars, unreadStyle, draftsStyle,
						        meString, draftString, draftPluralString, sendingString,
						        sendFailedString, forceAllUnread, forceAllRead);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                // Add unread count if configured
                //if (unreadCount > 1) {
                //    from.insert(0, "(" + unreadCount + ") ");
                //}
                found = true;
                break;
            }
        }
        
        
        String type = Notification.particularizeType(Notification.EMAIL, "gmail");
        type = Notification.particularizeType(type, tagLabel);
        return new Notification(fromAddress).setType(type).setExtra(subject);
	}
}
