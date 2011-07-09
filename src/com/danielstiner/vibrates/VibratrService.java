package com.danielstiner.vibrates;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

import android.app.Activity;
import android.app.ListActivity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

public class VibratrService extends Service {

	
	public static final String KEY_VIBRATE_DESCRIPTION = "com.danielstiner.vibrates.VibratrService.description";

	public static final String ACTION = "com.danielstiner.vibrates.VIBRATE";

	private Vibrator vibrator;
	
	private Manager ccm;
	
	// receives interactions from clients activities
    private final IBinder mBinder = new LocalBinder();
	
	public class LocalBinder extends Binder {
		VibratrService getService() {
            return VibratrService.this;
        }
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		ccm = new Manager(this);
		
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle bundle = intent.getExtras();
		
		// Use identifier to lookup the contact
		Entity entity = ccm.get(bundle.getString(Notification.IDENTIFIER_KEY));
		
		// If we have something, then vibrate away!
		if(entity != null)
			vibrator.vibrate(ccm.getPattern(entity, bundle.getString(Notification.TYPE_KEY)), -1);
		
		// TODO Fancy toast notifications
		// FIXME Based on user settings please
		String extra = bundle.getString(Notification.EXTRA_KEY);
		if(extra != null && entity != null)
				Toast.makeText(this, ccm.getDisplayName(entity)+": "+extra, Toast.LENGTH_SHORT).show();
		
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

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
}
