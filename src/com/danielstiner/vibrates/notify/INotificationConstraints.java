package com.danielstiner.vibrates.notify;

import com.danielstiner.vibrates.Entity;

public interface INotificationConstraints {

	public abstract boolean vibrate(Entity entity, String notificationType);

	public abstract boolean vibrate_default(String notificationType);

	public abstract boolean vibrate_group(Entity group, String notificationType);

	//boolean toast(Entity entity, String notificationType, String extra);

}
