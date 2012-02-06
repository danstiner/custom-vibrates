package com.danielstiner.vibrates.storage;

import roboguice.content.RoboContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.danielstiner.vibrates.storage.internal.IDatabase;
import com.google.inject.Inject;

public class VibrateEntityProvider extends RoboContentProvider {
	
	private static final String AUTHORITY = "com.danielstiner.vibrates.providers.VibrateEntityProvider";

	private static final String ENTITIES_BASE_PATH = "entities";
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ENTITIES_BASE_PATH);
	
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	        + "/danielstiner-vibrate-entity";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	        + "/danielstiner-vibrate-entity";
	
	@Inject
	private IDatabase mDatabase;
	
	@Override
	public boolean onCreate() {
		// TODO
		return true;
	}
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
