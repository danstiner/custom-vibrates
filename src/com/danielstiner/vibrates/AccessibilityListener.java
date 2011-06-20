package com.danielstiner.vibrates;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class AccessibilityListener extends AccessibilityService {
	
	@Override
	protected void onServiceConnected() {
		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC;
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		
		setServiceInfo(info);
	};

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), event.toString(), Toast.LENGTH_SHORT).show();
		
		//getClassName(), getPackageName(), getEventTime(), getText() getParcelableData()
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}

}
