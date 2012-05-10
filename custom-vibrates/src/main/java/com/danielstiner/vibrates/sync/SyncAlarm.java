package com.danielstiner.vibrates.sync;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SyncAlarm extends BroadcastReceiver {

	/** Max number of seconds between synchronizations */
	private static final int REPEAT_MAX_INTERVAL = (int) AlarmManager.INTERVAL_DAY;
	private static final String REMINDER_BUNDLE_KEY = null;

	/** Used by alarm manager */
	public SyncAlarm() {
	}

	/**
	 * Starts a sync after a randomized amount of time
	 * 
	 * @param context
	 */
	public static void start(Context context) {

		Bundle extras = new Bundle();

		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, SyncAlarm.class);
		intent.putExtra(REMINDER_BUNDLE_KEY, extras);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Add a randomized wait, up to REPEAT_MAX_INTERVAL
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(System.currentTimeMillis());
		time.add(Calendar.SECOND, rand(0, REPEAT_MAX_INTERVAL));

		// Schedule the alarm, be lazy and wait until the device wakes up
		alarmMgr.set(AlarmManager.RTC, time.getTimeInMillis(), pendingIntent);

	}

	/**
	 * 
	 * @param low
	 *            Lower bound inclusive
	 * @param high
	 *            Upper bound exclusive
	 * @return
	 */
	private static final int rand(int lower, int upper) {

		int offset = (int) ((upper - lower) * Math.random());

		return lower + offset;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// Simply start the synchronization service
		SyncService.performFullSync(context);

		// And set an alarm for the next synchronization to be performed
		start(context);
	}

}
