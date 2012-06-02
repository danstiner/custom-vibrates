package com.danielstiner.vibrates.notify;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.settings.IUserSettings;
import com.google.inject.Inject;

public class NotificationConstraints implements INotificationConstraints {

	private IUserSettings userSettings;

	// TODO Inject sensor
	@Inject
	public NotificationConstraints(IUserSettings userSettings) {
		this.userSettings = userSettings;
	}

	@Override
	public boolean vibrate_group(Entity group, String notificationType) {

		// check enabled user preference
		if (!this.userSettings.enabled() || group == null)
			return false;

		// TODO Check smart sensor / orientation
		return true;
	}

	@Override
	public boolean vibrate_default(String notificationType) {

		// check enabled user preference
		if (!this.userSettings.enabled())
			return false;

		// TODO Check smart sensor / orientation
		return true;
	}

	@Override
	public boolean vibrate(Entity entity, String notificationType) {

		// check enabled user preference
		if (!this.userSettings.enabled() || entity == null)
			return false;

		// TODO Check smart sensor / orientation
		return true;
	}

}
