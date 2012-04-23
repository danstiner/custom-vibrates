package com.danielstiner.vibrates.notify;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.settings.UserSettings;
import com.danielstiner.vibrates.util.IdentifierUtil;
import com.danielstiner.vibrates.util.NotificationTypes;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class IntentHandler implements IIntentHandler {

	// private static final String NOTIFY_GMAIL = NotificationTypes
	// .particularizeType(NotificationTypes.MESSAGE_EMAIL, "gmail");
	private static final String TYPE_NOTIFY_PHONE = NotificationTypes.VOICE_PHONECALL;
	private static final String TYPE_NOTIFY_SMS = NotificationTypes.CHAT_SMS;

	private static final String VIB_STATE = "android.media.VIBRATE_SETTING_CHANGED";
	private static final String RINGER_MODE = "android.media.RINGER_MODE_CHANGED";
	// private static final String WIFI_STATE =
	// "android.net.wifi.supplicant.STATE_CHANGE";
	private static final String PHONE_STATE = "android.intent.action.PHONE_STATE";
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	// private static final String BATTERY_STATE =
	// "android.intent.action.BATTERY_LOW";
	// private static final String DATA_SMS_STATE =
	// "android.intent.action.DATA_SMS_RECEIVED";
	// private static final String HEADSET_BT_STATE =
	// "android.bluetooth.headset.action.AUDIO_STATE_CHANGED";
	private static final String HEADSET_PLUG_STATE = "android.intent.action.HEADSET_PLUG";

	// Gmail constants
	// private static final Uri CONVERSATIONS_URI = Uri
	// .parse("content://gmail-ls/conversations/");

	// private static final String[] CONVERSATION_FIELDS = { "_id", "subject",
	// "snippet", "fromAddress", "date" };

	private Provider<VibrateNotify> notify_provider;

	private Provider<UserSettings> settings_provider;

	@Inject
	public IntentHandler(Provider<VibrateNotify> notify_provider,
			Provider<UserSettings> settings_provider) {
		this.notify_provider = notify_provider;
		this.settings_provider = settings_provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.danielstiner.vibrates.notify.IIntentHandler#handle(java.lang.String,
	 * android.os.Bundle, android.content.Context)
	 */
	public void handle(String action, Bundle bundle, Context context) {
		if (action.equals(SMS_RECEIVED)) {
			// retrieve the SMS message(s)
			Object[] pdus = (Object[]) bundle.get("pdus");
			for (int i = 0; i < pdus.length; i++) {
				handleSMS(SmsMessage.createFromPdu((byte[]) pdus[i]), context);
			}
		} else if (action.equals(PHONE_STATE)) {
			handlePhoneState(bundle, context);
		} else if (action.equals(Intent.ACTION_PROVIDER_CHANGED)) {
			// TODO: Handle gmail messages
			// handleProviderChange(bundle, context);
		} else if (action.equals(VIB_STATE)) {
			handleVibrateState(bundle, context);
		} else if (action.equals(RINGER_MODE)) {
			handleRingerMode(bundle, context);
		} else if (action.equals(HEADSET_PLUG_STATE)) {
			handleHeadsetPlugState(bundle, context);
		} else if (action.equals("android.media.VOLUME_CHANGED_ACTION")) {

		}
		// TODO Others
	}

	private void handleHeadsetPlugState(Bundle bundle, Context context) {
		// TODO Auto-generated method stub

	}

	private void handleRingerMode(Bundle bundle, Context context) {
		// Keep in vibrate mode if switched to normal ring mode
		if (settings_provider.get().keepInSilentMode()
				&& bundle.getInt(AudioManager.EXTRA_RINGER_MODE) != AudioManager.RINGER_MODE_SILENT) {

			AudioManager am = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);

			// Vibrate mode makes the pretty vibrate icon show up
			am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

			// // Only go to silent if we are enabled to replace the system
			// vibrates
			// if(settings_provider.get().enabled())
			// {
			// // Silent mode allows system vibrations for SMS/voice to be
			// turned off
			// am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			// }

			Toast.makeText(context, R.string.toast_forcesilent,
					Toast.LENGTH_SHORT).show();

			// bundle.get
			// String identifier =
			// bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			// String type = NotificationTypes.particularizeType(
			// NotificationTypes.HARDWARE, NOTIFY_PHONE);
			// notify_provider.get().identifier(identifier).type(type).fire(context);
		}
	}

	private void handleVibrateState(Bundle bundle, Context context) {
		// TODO Auto-generated method stub
		// String number =
		// bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		// String type =
		// VibrateNotify.particularizeType(VibrateNotify.TYPE_VOICE,
		// NOTIFY_PHONE);
		// notify_provider.get().identifier(number).type(type).fire(context);
	}

	private void handlePhoneState(Bundle bundle, Context context) {

		String state = bundle.getString(TelephonyManager.EXTRA_STATE);

		String number = bundle
				.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

		VibrateNotify notification = notify_provider.get().identifier(
				IdentifierUtil.phoneNumberToInternational(number),
				TYPE_NOTIFY_PHONE);

		// Check if we are currently ringing
		if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {

			notification.kind(VibrateNotify.Kind.Continuous).fire(context);

		} else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)
				|| TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {

			notification.kind(VibrateNotify.Kind.Cancel).fire(context);
			
		}
	}

	private void handleSMS(SmsMessage msg, Context context) {
		String number = msg.getDisplayOriginatingAddress();

		String international_number = IdentifierUtil
				.phoneNumberToInternational(number);

		String type = TYPE_NOTIFY_SMS;
		if (msg.isEmail())
			// Must have been an SMS from an email address
			type = NotificationTypes.particularizeType(type, "emailed");

		// throw in the body of the message for good measure
		String extra = msg.getDisplayMessageBody();

		// Will tell the vibrator service to get busy
		notify_provider.get().identifier(international_number, type)
				.extra(extra).fire(context);
	}

}
