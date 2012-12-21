package com.danielstiner.vibrates.service;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public abstract class AbstractSmsMmsContentObserver extends ContentObserver {

	public static final Uri URI = Uri.parse("content://sms");
	//Uri.parse("content://mms-sms")

	public AbstractSmsMmsContentObserver(Handler handler) {
		super(handler);
	}

}
