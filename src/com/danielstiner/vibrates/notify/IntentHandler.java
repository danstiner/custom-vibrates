package com.danielstiner.vibrates.notify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.settings.UserSettings;
import com.danielstiner.vibrates.utility.NotificationTypes;
import com.google.inject.Inject;
import com.google.inject.Provider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Toast;

public class IntentHandler implements IIntentHandler {
	
	private static final String NOTIFY_GMAIL = NotificationTypes.particularizeType(NotificationTypes.MESSAGE_EMAIL, "gmail");
	private static final String NOTIFY_PHONE = NotificationTypes.particularizeType(NotificationTypes.VOICE, "phone");
	private static final String NOTIFY_SMS = NotificationTypes.CHAT_SMS;
	
	private static final String LOG_TAG = "CustomVibrates";

	private static final String VIB_STATE = "android.media.VIBRATE_SETTING_CHANGED";
	private static final String RINGER_MODE = "android.media.RINGER_MODE_CHANGED";
	private static final String WIFI_STATE = "android.net.wifi.supplicant.STATE_CHANGE";
	private static final String PHONE_STATE = "android.intent.action.PHONE_STATE";
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String BATTERY_STATE = "android.intent.action.BATTERY_LOW";
	private static final String DATA_SMS_STATE = "android.intent.action.DATA_SMS_RECEIVED";
	private static final String HEADSET_BT_STATE = "android.bluetooth.headset.action.AUDIO_STATE_CHANGED";
	private static final String HEADSET_PLUG_STATE = "android.intent.action.HEADSET_PLUG";
	
	// Gmail constants
    private static final Uri CONVERSATIONS_URI = Uri.parse("content://gmail-ls/conversations/");

    private static final String[] CONVERSATION_FIELDS = {
            "_id", "subject", "snippet", "fromAddress", "date"
    };
    
    private Provider<VibrateNotify> notify_provider;
    
    private Provider<UserSettings> settings_provider;
    
    @Inject
    public IntentHandler(Provider<VibrateNotify> notify_provider, Provider<UserSettings> settings_provider)
    {
    	this.notify_provider = notify_provider;
    	this.settings_provider = settings_provider;
    }

	/* (non-Javadoc)
	 * @see com.danielstiner.vibrates.notify.IIntentHandler#handle(java.lang.String, android.os.Bundle, android.content.Context)
	 */
	public void handle(String action, Bundle bundle, Context context) {
		if (action.equals(SMS_RECEIVED)) {
			// retrieve the SMS message(s)
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				handleSMS(SmsMessage.createFromPdu((byte[])pdus[i]), context);
			}
		} else if(action.equals(PHONE_STATE)) {
			handlePhoneState(bundle, context);
		} else if(action.equals(Intent.ACTION_PROVIDER_CHANGED)) {
			handleProviderChange(bundle, context);
		} else if(action.equals(VIB_STATE)) {
			handleVibrateState(bundle, context);
		} else if(action.equals(RINGER_MODE)) {
			handleRingerMode(bundle, context);
		} else if(action.equals(HEADSET_PLUG_STATE)) {
			handleHeadsetPlugState(bundle, context);
		} else if(action.equals("android.media.VOLUME_CHANGED_ACTION")) {
			
		}
		// TODO Others
	}
	
	private void handleHeadsetPlugState(Bundle bundle, Context context) {
		// TODO Auto-generated method stub
		
	}
	
	private void handleRingerMode(Bundle bundle, Context context) {
		// Keep in vibrate mode if switched to normal ring mode
		if(settings_provider.get().keepInSilentMode()
				&& bundle.getInt(AudioManager.EXTRA_RINGER_MODE) != AudioManager.RINGER_MODE_SILENT) {
			
			AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE); 
			
			// Vibrate mode makes the pretty vibrate icon show up
			am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			
//			// Only go to silent if we are enabled to replace the system vibrates
//			if(settings_provider.get().enabled())
//			{
//				// Silent mode allows system vibrations for SMS/voice to be turned off
//				am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//			}
			
			Toast.makeText(context, R.string.toast_forcesilent, Toast.LENGTH_SHORT).show();
			
			//bundle.get
			//String identifier = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			String type = NotificationTypes.particularizeType(NotificationTypes.HARDWARE, NOTIFY_PHONE);
			//notify_provider.get().identifier(identifier).type(type).fire(context);
		}
	}

	private void handleVibrateState(Bundle bundle, Context context) {
		// TODO Auto-generated method stub
		//String number = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		//String type = VibrateNotify.particularizeType(VibrateNotify.TYPE_VOICE, NOTIFY_PHONE);
		//notify_provider.get().identifier(number).type(type).fire(context);
	}

	private void handlePhoneState(Bundle bundle, Context context) {
		// Check if we are currently ringing
		if (bundle.getString(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			// Hand off handling of this event
			String number = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			String type = NotificationTypes.particularizeType(NotificationTypes.VOICE, NOTIFY_PHONE);
			VibrateNotify n = notify_provider.get();
			n.identifier(number).type(type);
			n.fire(context);
		}
	}

	private void handleSMS(SmsMessage msg, Context context) {
		String ident = msg.getDisplayOriginatingAddress();
		
		String type = NOTIFY_SMS;
		if (msg.isEmail())
			// Must have been an SMS from an email address
			type = NotificationTypes.particularizeType(type, "emailed");

		// throw in the body of the message for good measure
		String extra = msg.getDisplayMessageBody();

		// Will tell the vibrator service to get busy
		notify_provider.get().identifier(ident).type(type).extra(extra).fire(context);
	}
	private void handleProviderChange(Bundle bundle, Context context) {
		// TODO tmp
		if(bundle == null || bundle != null)
			return;
		
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
					return;
				}
				 
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
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
        
        
        String type = NOTIFY_GMAIL;
        type = NotificationTypes.particularizeType(type, tagLabel);
        notify_provider.get().identifier(fromAddress).type(type).extra(subject).fire(context);
        //return new VibrateNotify(fromAddress).setType(type).setExtra(subject);
	}

}
