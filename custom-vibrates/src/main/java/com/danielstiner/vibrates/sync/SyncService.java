package com.danielstiner.vibrates.sync;

import roboguice.service.RoboIntentService;
import android.content.Context;
import android.content.Intent;

public class SyncService extends RoboIntentService {

	public SyncService(String name) {
		super(name);
	}

	/**
	 * Starts this sync service in the given context
	 * 
	 * Will then synchronize information between the system contact list and our
	 * app's internal contact list
	 * 
	 * @param context
	 *            Context to run service in
	 * */
	public static void performFullSync(Context context) {
		// TODO
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub

	}

}
