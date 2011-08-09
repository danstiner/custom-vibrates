package com.danielstiner.vibrates.notify;

import roboguice.service.RoboService;
import roboguice.util.Ln;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.database.IManager;
import com.google.inject.Inject;
import com.google.inject.Provider;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;

public class VibratrService extends RoboService {
	
	@Inject Provider<VibrateNotify> notify_provider;
	
	@Inject INotificationConstraints notify_constraints;
	
	@Inject IManager manager;
	
	@Inject Vibrator vibrator;
	
	public static final String EXTRA_KEY_VIBRATE_DESCRIPTION = "com.danielstiner.vibrates.VibratrService.description";

	public static final String ACTION = "com.danielstiner.vibrates.VIBRATE";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle bundle = intent.getExtras();
		
		// Extract what just happened
		VibrateNotify n = notify_provider.get();
		n.loadBundle(bundle);
		
		// Try an find the associated entity
		Entity e = manager.getEntity(n.identifier());
		
		if(e == null)
			Ln.d("Got a null notification");
		else
			Ln.v(
				"VibratrService Notify: got entity #%s for %s because %s",
				e.entityid().toString(),
				n.identifier(),
				n.type()
				);
		
		// If we have something, then vibrate away!
		if(notify_constraints.vibrate(e, n.type()))
		{
			vibrator.vibrate(manager.getPattern(e, n.type()), -1);
		}
		
//		// Fancy toast notifications
//		String extra = bundle.getString(com.danielstiner.vibrates.notify.EXTRA_KEY);
//		if(notify_constraints.toast(entity, notification_type, extra))
//		{
//			Toast.makeText(this, entity_manager.getDisplayName(entity)+": "+extra, Toast.LENGTH_SHORT).show();
//		}
		
		// We are lightweight to start, and notifications do not come often enough
		// to justify sticking around forever, so just exit
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Cancel any ongoing notification.
		vibrator.cancel();
	}

	/**
	 * No need to bind to this service, it is a consumer only
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
