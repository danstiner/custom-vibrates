package com.danielstiner.vibrates.storage.internal;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

public class AsyncEntityLoader extends AsyncTaskLoader<Cursor> {

	public AsyncEntityLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Cursor loadInBackground() {
		// TODO Auto-generated method stub
		return null;
	}

}
