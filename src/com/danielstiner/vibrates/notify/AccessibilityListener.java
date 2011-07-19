package com.danielstiner.vibrates.notify;

import com.google.inject.Inject;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityListener extends AccessibilityService {
	@Inject AccessibilityHandler handler;
	
	@Override
	protected void onServiceConnected() {
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC;
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		setServiceInfo(info);
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
