package com.danielstiner.vibrates.notify;

import roboguice.RoboGuice;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;

import com.google.inject.Inject;

public class AccessibilityListener extends AccessibilityService {
	@Inject
	AccessibilityHandler handler;

	@Override
	protected void onServiceConnected() {
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC;
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		setServiceInfo(info);

		// Setup injection manually since we do not extend a Robo class
		RoboGuice.injectMembers(getApplicationContext(), this);
	};

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		handler.handle(event, getApplicationContext());
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

}
