package com.danielstiner.vibrates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IntentReciever extends BroadcastReceiver {

	private static final String LOG_TAG = "CustomVibrates";

	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

	private static final String PHONE_STATE = "android.intent.action.PHONE_STATE";

	@Override
	public void onReceive(Context context, Intent intent) {
		// Check that we have some extra info to actually work with
		Bundle bundle = intent.getExtras();
		
		if(bundle == null) return;
		
		// Figure out what kind of intent we have
		if (intent.getAction().equals(SMS_RECEIVED)) {

			// retrieve the SMS message(s)
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				handleSMS(SmsMessage.createFromPdu((byte[])pdus[i]), context);
			}

		} else if(intent.getAction().equals(PHONE_STATE)) {
			handlePhoneState(bundle, context);
		}
	}

	private void handlePhoneState(Bundle bundle, Context context) {
		String state = bundle.getString(TelephonyManager.EXTRA_STATE);
		if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			String phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			// TODO
		}
	}

	private void handleSMS(SmsMessage msg, Context context) {

		// To hand off handling of this event
		//Intent service = new Intent(context, VibratrService.class);
		Intent service = new Intent(VibratrService.ACTION);

		// Put in common data, like the address the message is from
		service.putExtra(CustomContactManager.KEY_VIBRATE_IDENTIFIER, msg
				.getDisplayOriginatingAddress());
		// and the body of the message
		service.putExtra(VibratrService.KEY_VIBRATE_DESCRIPTION, msg
				.getDisplayMessageBody());

		if (msg.isEmail()) {
			// Must have been an SMS from an email address
			// treat it as an actual email
			service.putExtra(CustomContactManager.KEY_VIBRATE_MIMETYPE,
					CustomContactManager.MIMETYPE_EMAIL);

		} else {
			// Must have been a normal SMS
			service.putExtra(CustomContactManager.KEY_VIBRATE_MIMETYPE,
					CustomContactManager.MIMETYPE_SMS);
		}

		// Tell the vibrator service to get busy
		context.startService(service);
	}

}
