package com.danielstiner.vibrates.model.internal;

import roboguice.content.RoboContentProvider;
import roboguice.inject.ContextScopedProvider;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
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

	private static final String TYPE_SINGLE = "vnd.android.cursor.item/com.danielstiner.vibrates.entity";

	private static final String TYPE_DIR = "vnd.android.cursor.dir/com.danielstiner.vibrates.entity";

	private static final UriMatcher uriMatcher;

	private static final int URI_ENTITIES = 1;

	private static final int URI_ENTITY_ID = 2;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "entities", URI_ENTITIES);
		uriMatcher.addURI(AUTHORITY, "entities/#", URI_ENTITY_ID);
	}

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
			return mDatabaseProvider.get(mContextProvider.get())
					.getWritableDatabase()
					.delete(TABLE, selection, selectionArgs);
			// groupBy,
			// having,
			// orderBy)
		}

		int query_type = uriMatcher.match(uri);

		switch (query_type) {
		case URI_ENTITIES:
			// All entities
			return mDatabaseProvider.get(mContextProvider.get())
					.getWritableDatabase()
					.delete(TABLE, selection, selectionArgs);
		case URI_ENTITY_ID:
			// single entity
			return mDatabaseProvider
					.get(mContextProvider.get())
					.getWritableDatabase()
					.delete(TABLE, Entities.ENTITY_ID + " == ?",
							new String[] { uri.getPathSegments().get(1) });
		}

		return 0;
	}

	@Override
	public String getType(Uri uri) {
		int query_type = uriMatcher.match(uri);

		switch (query_type) {
		case URI_ENTITIES:
			// All entities
			return TYPE_DIR;
		case URI_ENTITY_ID:
			// single entity
			return TYPE_SINGLE;
		}

		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (CONTENT_URI.equals(uri)) {
			long insert_id = mDatabaseProvider.get(mContextProvider.get())
					.getWritableDatabase().insert(TABLE, null, values);

			if (insert_id < 0)
				return null;

			return Entities.getEntityUri(insert_id);

		}

		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		int query_type = uriMatcher.match(uri);

		switch (query_type) {
		case URI_ENTITIES:
			// All entities
			return mDatabaseProvider
					.get(mContextProvider.get())
					.getReadableDatabase()
					.query(TABLE, projection, selection, selectionArgs, null,
							null, null);
		case URI_ENTITY_ID:
			// single entity
			return mDatabaseProvider
					.get(mContextProvider.get())
					.getReadableDatabase()
					.query(TABLE, projection, Entities.ENTITY_ID + " == ?",
							new String[] { uri.getPathSegments().get(1) },
							null, null, null);
		}

		return null;
	}

	@Override
	public int update(Uri uri, ContentValues cv, String selection,
			String[] selectionArgs) {
		int query_type = uriMatcher.match(uri);

		switch (query_type) {
		case URI_ENTITIES:
			// All entities
			return mDatabaseProvider.get(mContextProvider.get())
					.getWritableDatabase()
					.update(TABLE, cv, selection, selectionArgs);
		case URI_ENTITY_ID:
			// single entity
			return mDatabaseProvider
					.get(mContextProvider.get())
					.getWritableDatabase()
					.update(TABLE, cv, Entities.ENTITY_ID + " == ?",
							new String[] { uri.getPathSegments().get(1) });
		}
		return 0;
	}

}