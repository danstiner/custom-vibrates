package com.danielstiner.vibrates.notify;

import roboguice.util.Ln;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;

import com.danielstiner.vibrates.model.Identifiers;
import com.danielstiner.vibrates.util.NotificationTypes;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AccessibilityHandler {

	private static final String GOOGLE_VOICE_PACKAGE = "com.google.android.apps.googlevoice";
	private static final String GOOGLE_VOICE_TYPE = NotificationTypes
			.particularizeType(NotificationTypes.CHAT_SMS, "google_voice");

	private static final String FACEBOOK_PACKAGE = "com.facebook.orca";
	private static final String FACEBOOK_TYPE = NotificationTypes
			.particularizeType(NotificationTypes.CHAT_IM, "facebook");

	private static final String GOOGLE_TALK_PACKAGE = "com.google.android.gsf";
	private static final String GOOGLE_TALK_TYPE = NotificationTypes
			.particularizeType(NotificationTypes.CHAT_IM, "google_talk");

	private static final String GOOGLE_GMAIL_PACKAGE = "com.google.android.gm";
	private static final String GOOGLE_GMAIL_TYPE = NotificationTypes.MESSAGE_EMAIL;

	private Provider<VibrateNotify> notify_provider;

	@Inject
	public AccessibilityHandler(Provider<VibrateNotify> notify_provider) {
		this.notify_provider = notify_provider;
	}

	public void handle(AccessibilityEvent event, Context context) {
		// android.app.Notification notification = (android.app.Notification)
		// event
		// .getParcelableData();

		String packageName = event.getPackageName().toString();

		if (packageName.equals(GOOGLE_VOICE_PACKAGE))
			handleChat(event, context, GOOGLE_VOICE_TYPE);
		else if (packageName.equals(GOOGLE_GMAIL_PACKAGE))
			handleGmail(event, context);
		else if (packageName.equals(FACEBOOK_PACKAGE))
			handleChat(event, context, FACEBOOK_TYPE);
		else if (packageName.equals(GOOGLE_TALK_PACKAGE))
			handleChat(event, context, GOOGLE_TALK_TYPE);
	}

	private void handleChat(AccessibilityEvent event, Context context,
			String chat_type) {

		VibrateNotify n = chatNotification(event);

		if (n != null)
			n.source_type(chat_type).fire(context);
	}

	private VibrateNotify chatNotification(AccessibilityEvent event) {
		if (event.getText().size() < 1) {
			Ln.e("Bad chat notifcation: no event text!");
			return null;
		}

		// Split the notification up
		String[] message_parts = event.getText().get(0).toString()
				.split(": ", 2);
		if (message_parts.length != 2) {
			Ln.e("Bad chat notifcation: no colon separated name:message!");
			return null;
		}

		String name = message_parts[0].trim();
		String text = message_parts[1];
		return notify_provider.get().identifier(name, Identifiers.Common.Name)
				.extra(text);
	}

	private void handleGmail(AccessibilityEvent event, Context context) {

		String name = event.getText().get(0).toString();

		notify_provider.get().identifier(name, Identifiers.Common.Name)
				.source_type(GOOGLE_GMAIL_TYPE).fire(context);
	}

}
