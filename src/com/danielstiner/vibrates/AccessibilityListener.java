package com.danielstiner.vibrates;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class AccessibilityListener extends AccessibilityService {
	
	private static final String GOOGLE_VOICE_PACKAGE = "com.google.android.apps.googlevoice";
	private static final String GOOGLE_VOICE_TYPE = Notification.particularizeType(Notification.SMS, "google_voice");
	
	
	@Override
	protected void onServiceConnected() {
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC;
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		setServiceInfo(info);
	};

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub\
		android.app.Notification notification = (android.app.Notification) event.getParcelableData();
		//Toast.makeText(getBaseContext(), event.getPackageName(), Toast.LENGTH_SHORT).show();
		//Parcelable p = event.getParcelableData();
		// Parse our accessibility event
		//new Notification
		if(event.getPackageName().toString().equals(GOOGLE_VOICE_PACKAGE))
			handleGoogleVoice(event).fire(getApplicationContext());
		
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}
	
	private Notification handleGoogleVoice(AccessibilityEvent event) {
		if(event.getText().size() < 1)
			return handleError("Bad Google Voice notifcation: no text!");
		
		// Split the notification up
		String[] message_parts = event.getText().get(0).toString().split(":", 2);
		if(message_parts.length != 2)
			return handleError("Bad Google Voice notifcation: no colon seperated name:message!");
		
		String name = message_parts[0];
		String text = message_parts[1];
		return new Notification(name, GOOGLE_VOICE_TYPE).setExtra(text);
	}
	private Notification handleError(String error_message) {
		return new Notification(null).setExtra(error_message);
	}
}
