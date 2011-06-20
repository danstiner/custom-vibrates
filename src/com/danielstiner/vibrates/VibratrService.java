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
	
	private CustomContactManager ccm;
	
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
		
		ccm = new CustomContactManager(this);
		
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Contact contact = null;
		
		Bundle bundle = intent.getExtras();
		
		
		// We at least need an identifier to lookup for a mimetype
		String mimetype = bundle.getString(CustomContactManager.KEY_VIBRATE_MIMETYPE);
		String identifier = bundle.getString(CustomContactManager.KEY_VIBRATE_IDENTIFIER);
		
		if(mimetype != null && identifier != null) {
			// Try and find the corresponding contact
			
			// Lookup by the given mimetype first
			contact = ccm.getContact(mimetype, identifier);
			
			// If we have something, then vibrate away!
			if(contact != null)
				vibrator.vibrate(contact.getPattern(mimetype), -1);
			
			// TODO combined contact + service patterns
			
		}
		
		String description = bundle.getString(KEY_VIBRATE_DESCRIPTION);
		
		// TODO Fancy toast notifications
		if(description != null) {
			// include contact info if possible
			if(contact != null)
				Toast.makeText(this, contact.getDisplayName()+":"+description, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
		}
		
		
		
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
