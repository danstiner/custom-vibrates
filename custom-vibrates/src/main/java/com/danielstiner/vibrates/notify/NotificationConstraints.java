package com.danielstiner.vibrates.notify;

import roboguice.util.Ln;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.settings.IUserSettings;
import com.google.inject.Inject;

public class NotificationConstraints implements INotificationConstraints {

	private static final long MAX_ACCEL_WAIT = 100;
	private IUserSettings userSettings;

	@Inject
	private SensorManager mSensorManager;
	private float mX;
	private float mY;
	private float mZ;
	private float mProx;
	protected final SensorEventListener mAccelListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// Ignore
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			mProx = event.values[0];
		}

	};

	protected final SensorEventListener mProxListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// Ignore
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			mX = event.values[0];
			mY = event.values[1];
			mZ = event.values[2];
		}

	};

	private Object mSensorSync = new Object();

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

	@Override
	public boolean sound(Entity e, String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sound_default(String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sound_group(Entity group, String type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean toast(Entity e, String type) {
		if (userSettings.enabled() && userSettings.toast() && e != null)
			return true;
		return false;
	}

	@Override
	public boolean toast_default(String type) {
		if (userSettings.enabled() && userSettings.toast())
			return true;
		return false;
	}

	@Override
	public boolean toast_group(Entity group, String type) {
		if (userSettings.enabled() && userSettings.toast() && group != null)
			return true;
		return false;
	}

	private boolean checkIsFlat() {

		synchronized (mSensorSync) {
			mX = 0;
			mY = 0;
			mZ = 0;
			mProx = Float.MAX_VALUE;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				Sensor sensor_accel = mSensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

				Sensor sensor_prox = mSensorManager
						.getDefaultSensor(Sensor.TYPE_PROXIMITY);

				// Check that we may continue (ie have a sensor to use)
				if (sensor_accel == null || sensor_prox == null) {
					synchronized (NotificationConstraints.this.mSensorSync) {
						NotificationConstraints.this.mSensorSync.notify();
					}
				}

				// Register a listener for accel and prox, it will take of the
				// rest
				mSensorManager.registerListener(mAccelListener, sensor_accel,
						SensorManager.SENSOR_DELAY_FASTEST);
				mSensorManager.registerListener(mProxListener, sensor_prox,
						SensorManager.SENSOR_DELAY_FASTEST);
			}

		}).start();

		//
		try {
			synchronized (mSensorSync) {
				this.mSensorSync.wait(MAX_ACCEL_WAIT);
			}

		} catch (InterruptedException e) {
			Ln.d(e, "Interrupted in NotificationConstraints while"
					+ " calculating checkIsFlat");

		}

		return false;

	}

}
