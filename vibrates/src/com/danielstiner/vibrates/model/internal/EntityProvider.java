package com.danielstiner.vibrates.model.internal;

import roboguice.content.RoboContentProvider;
import roboguice.inject.ContextScopedProvider;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.danielstiner.vibrates.model.Entities;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntityProvider extends RoboContentProvider {

	private static final String AUTHORITY = "com.danielstiner.vibrates.providers.Entity";

	private static final String ENTITIES_BASE_PATH = "entities";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + ENTITIES_BASE_PATH);

	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/danielstiner-vibrate-entity";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/danielstiner-vibrate-entity";

	private static final String TABLE = Entities.TABLE;

	@Inject
	private ContextScopedProvider<IDatabase> mDatabaseProvider;
	
	@Inject
	private Provider<Application> mContextProvider;

	@Override
	public boolean onCreate() {
		super.onCreate();
		// TODO
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		if (CONTENT_URI.equals(uri)) {
			return mDatabaseProvider.get(mContextProvider.get()).getWritableDatabase().delete(TABLE, selection, selectionArgs);
			// groupBy,
			// having,
			// orderBy)
		}
		
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (CONTENT_URI.equals(uri)) {
			long insert_id = mDatabaseProvider.get(mContextProvider.get()).getWritableDatabase().insert(TABLE,
					null, values);

			if (insert_id < 0)
				return null;

			return getEntityUriFromId(insert_id);

		}

		return null;
	}

	private Uri getEntityUriFromId(long insert_id) {
		// TODO Auto-generated method stub
		return Uri
				.withAppendedPath(CONTENT_URI, "#" + Long.toString(insert_id));
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		if (CONTENT_URI.equals(uri)) {
			return mDatabaseProvider.get(mContextProvider.get()).getReadableDatabase().query(TABLE, projection,
					selection, selectionArgs, null, null, null);
			// groupBy,
			// having,
			// orderBy)
		}

		return null;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}