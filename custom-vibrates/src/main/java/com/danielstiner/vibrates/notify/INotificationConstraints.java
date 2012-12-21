package com.danielstiner.vibrates.notify;

import com.danielstiner.vibrates.Entity;

public interface INotificationConstraints {

	public abstract boolean vibrate(Entity entity, String notificationType);

	public abstract boolean vibrate_default(String notificationType);

	public abstract boolean vibrate_group(Entity group, String notificationType);

	public abstract boolean sound_default(String type);

	public abstract boolean sound(Entity e, String type);

	public abstract boolean sound_group(Entity group, String type);

	public abstract boolean toast_group(Entity group, String type);

	public abstract boolean toast(Entity e, String type);

	public abstract boolean toast_default(String type);

}
