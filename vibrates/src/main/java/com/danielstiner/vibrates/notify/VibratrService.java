package com.danielstiner.vibrates.notify;

import java.util.Timer;

import roboguice.service.RoboService;
import roboguice.util.Ln;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.database.IManager;
import com.danielstiner.vibrates.settings.IUserSettings;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class VibratrService extends RoboService {

	@Inject
	Provider<VibrateNotify> mNotification_provider;

	@Inject
	Provider<IUserSettings> mUser_settings_provider;

	@Inject
	INotificationConstraints mNotify_constraints;

	@Inject
	IManager mManager;

	@Inject
	Vibrator mVibrator;

	@Inject
	Timer mTimer;

	@Inject
	Provider<AbstractPatternPlayer> mPattern_player_provider;

	@Inject
	PowerManager mPowerManager;

	private WakeLock mWakeLock;

	public static final String EXTRA_KEY_VIBRATE_DESCRIPTION = "com.danielstiner.vibrates.VibratrService.description";

	public static final String ACTION = "com.danielstiner.vibrates.VIBRATE";

	private static final String WAKE_LOCK_TAG = "com.danielstiner.vibrates.VibratrService";

	private static final long WAKE_LOCK_TIMEOUT = 10000; // 10 seconds

	@Override
	public void onCreate() {
		super.onCreate();

		// Get wake locker
		mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				WAKE_LOCK_TAG);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Acquire wake lock
		mWakeLock.acquire(WAKE_LOCK_TIMEOUT);

		Bundle bundle = intent.getExtras();

		// Extract what just happened
		// (The notification that caused this service to be called)
		VibrateNotify notification = mNotification_provider.get();
		notification.loadFromBundle(bundle);

		// Pass off work to private helper
		handleNotification(notification, (Context) this, mManager,
				mNotify_constraints, mUser_settings_provider, mVibrator,
				mTimer, mPattern_player_provider);

		// Release wake lock
		mWakeLock.release();

		// We are lightweight to start, and notifications do not come often
		// enough
		// to justify sticking around forever, so just exit
		// TODO: Auto stop sometime after the pattern finishes please using the
		// timer!
		// stopSelf();
		return START_NOT_STICKY;
	}

	private static void handleNotification(VibrateNotify notification,
			Context context, IManager manager,
			INotificationConstraints notify_constraints,
			Provider<IUserSettings> user_settings_provider, Vibrator vibrator,
			Timer timer, Provider<AbstractPatternPlayer> pattern_player_provider) {
		// Grab the notification's identifier and type
		String identifier = notification.identifier();
		String type = notification.type();

		// Try and find the associated group
		Entity group = manager.getEntity(identifier, Entity.TYPE_GROUP);

		// Try and find the associated entity
		Entity e = manager.getEntity(identifier, Entity.TYPE_CONTACT);

		// Do vibrations
		if (e == null && group == null) {
			Ln.d("VibratrService Notify: Got a null notification, playing default");

			// Play a default
			if (notify_constraints.vibrate_default(type)) {
				// vibrator.vibrate(user_settings_provider.get().defaultPattern(),
				// -1);
				vibrate(user_settings_provider.get().defaultPattern(type),
						type, vibrator, timer, pattern_player_provider);
			}
		} else if (notify_constraints.vibrate(e, type)) {
			vibrate(manager.getPattern(e), type, vibrator, timer,
					pattern_player_provider);
		} else if (notify_constraints.vibrate_group(group, type)) {
			vibrate(manager.getPattern(group), type, vibrator, timer,
					pattern_player_provider);
		}

		// Play call sounds
		if (e == null && group == null) {
			Ln.d("VibratrService Notify: Got a null notification, playing default");

			// Play a default
			if (notify_constraints.sound_default(type)) {
				// vibrator.vibrate(user_settings_provider.get().defaultPattern(),
				// -1);
				sound(user_settings_provider.get().defaultPattern(type), type);
			}
		} else if (notify_constraints.sound(e, type)) {
			sound(manager.getPattern(e), type);
		} else if (notify_constraints.sound_group(group, type)) {
			sound(manager.getPattern(group), type);
		}

		// Show toast notification
		if (e == null && group == null) {

			// Play a default
			if (notify_constraints.toast_default(type)) {
				toast(user_settings_provider.get().defaultPattern(type), type,
						null, context);
			}
		} else if (notify_constraints.toast(e, type)) {
			toast(manager.getPattern(e), type, e, context);
		} else if (notify_constraints.toast_group(group, type)) {
			toast(manager.getPattern(group), type, group, context);
		}
	}

	private static void sound(Pattern pattern, String type) {
		// TODO Auto-generated method stub

	}

	private static void vibrate(Pattern p, String type, Vibrator vibrator,
			Timer timer, Provider<AbstractPatternPlayer> pattern_player_provider) {
		// Schedule the pattern to be played as soon as possible
		timer.schedule(
				pattern_player_provider.get().pattern(p).vibrator(vibrator)
						.timer(timer), 0);
	}

	private static void toast(Pattern pattern, String type, Entity e,
			Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Cancel any ongoing notification.
		mTimer.cancel();
		mVibrator.cancel();
	}

	/**
	 * No need to bind to this service, it is a consumer only
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
