package com.danielstiner.vibrates.notify;

import android.hardware.Sensor;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.settings.IUserSettings;
import com.danielstiner.vibrates.settings.UserSettings;
import com.google.inject.Inject;

public class NotificationConstraints implements INotificationConstraints {

	private IUserSettings userSettings;

	// TODO Inject sensor
	@Inject
	public NotificationConstraints(IUserSettings userSettings)
	{
		this.userSettings = userSettings;
	}
	
	@Override
	public boolean vibrate(Entity entity, String notificationType) {
		// TODO Auto-generated method stub
		
		// check enabled user preference
		if(!this.userSettings.enabled())
			return false;
		
		// TODO Check smart sensor / orientation
		return true;
	}

}
