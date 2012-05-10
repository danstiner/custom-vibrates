package com.danielstiner.vibrates.service;

import android.os.PowerManager.WakeLock;

import com.danielstiner.vibrates.notify.VibrateNotify;

public interface INotificationHandler {

	void handle(VibrateNotify notification);

	void setOnIdle(Runnable runnable);

	void setWakeLock(WakeLock mWakeLock);

	void cancel();

}
