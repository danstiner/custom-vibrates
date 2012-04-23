package com.danielstiner.vibrates.service;

import roboguice.inject.ContextScopedProvider;
import roboguice.service.RoboService;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.danielstiner.vibrates.notify.VibrateNotify;
import com.danielstiner.vibrates.settings.IUserSettings;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class VibratrService extends RoboService {

	private static final String ACTION = "com.danielstiner.vibrates.VIBRATE";

	@Inject
	private Provider<IUserSettings> mUserSettingsProvider;

	@Inject
	private Provider<VibrateNotify> mNotification_provider;

	@Inject
	private ContextScopedProvider<INotificationHandler> mHandlerProvider;

	private INotificationHandler mHandler;

	@Inject
	private PowerManager mPowerManager;

	@Inject
	private Provider<Application> mContextProvider;

	private WakeLock mWakeLock;

	private static final String WAKE_LOCK_TAG = "com.danielstiner.vibrates.VibratrService";

	private static final long WAKE_LOCK_TIMEOUT = 10000; // 10 seconds

	public static void start(Intent i, Context context) {
		i.setAction(VibratrService.ACTION);
		context.startService(i);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mHandler = mHandlerProvider.get(mContextProvider.get());

		// Get wake locker
		mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				WAKE_LOCK_TAG);

		mHandler.setWakeLock(mWakeLock);
		mHandler.setOnIdle(new Runnable() {

			@Override
			public void run() {

				// Stop service if user doesn't want it sticking around
				if (mUserSettingsProvider.get().backgroundService() == false)
					VibratrService.this.stopSelf();
			}

		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Acquire wake lock
		mWakeLock.acquire(WAKE_LOCK_TIMEOUT);

		// Extract what just happened
		// (The notification that caused this service to be called)
		VibrateNotify notification = mNotification_provider.get().loadBundle(
				intent.getExtras());

		// Pass off work to private helper
		mHandler.handle(notification);

		// Release wake lock
		mWakeLock.release();

		// We are lightweight to start, once the handle queue is empty, this
		// service will end itself
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Cancel any ongoing notification.
		mHandler.cancel();
	}

	/**
	 * No need to bind to this service, it is a consumer only
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
