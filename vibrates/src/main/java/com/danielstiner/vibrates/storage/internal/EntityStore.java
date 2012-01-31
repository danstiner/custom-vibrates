package com.danielstiner.vibrates.storage.internal;

import java.util.Arrays;

import roboguice.util.Ln;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.storage.internal.IDatabase.IDatabaseHelper;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntityStore implements IEntityStore {

	static final String NS = com.danielstiner.vibrates.Vibrates.NS + "."
			+ "database";
	static final String CLASSNAME = NS + "." + "EntityManager";

	private static final int VERSION = Database.VERSION;

	protected static final String TABLE = "entities";

	protected static final String KEY_ID = "_id";
	protected static final String KEY_KIND = "kind";
	protected static final String KEY_NAME = "name";
	protected static final String KEY_PATTERN = "pattern";
	protected static final String KEY_NOTIFY_COUNT = "notified";

	protected static final String EXTRA_CACHE_KEY_NAME = CLASSNAME + "."
			+ KEY_NAME;
	protected static final String EXTRA_CACHE_KEY_KIND = CLASSNAME + "."
			+ KEY_KIND;
	protected static final String EXTRA_CACHE_KEY_PATTERN = CLASSNAME + "."
			+ KEY_PATTERN;
	protected static final String EXTRA_CACHE_KEY_NOTIFY_COUNT = CLASSNAME
			+ "." + KEY_NOTIFY_COUNT;

	@Inject
	private IDatabase _db;

	private Provider<Entity> entity_provider;

	// @Inject
	// private Context context;

	@Inject
	public EntityStore(Provider<Entity> entity_provider) {
		this.entity_provider = entity_provider;
	}

	// /**
	// *
	// * @param c
	// * @return
	// */
	// public static Uri getLookupUri(Entity c) {
	// // FIXME: Don't assume the identier is the contact lookup, or the id even
	// return ContactsContract.Contacts.getLookupUri(c.entityid(),
	// c.identifier());
	// }

	@Override
	public Entity create(String name, long[] pattern, String type) {
		long created_id = -2;
		// First open a connection to the database
		SQLiteDatabase db = _db.getWritableDatabase();
		try {
			// Perform insert of actual entity
			ContentValues entity_values = new ContentValues(4);
			entity_values.put(KEY_NAME, name);
			entity_values.put(KEY_KIND, type);
			entity_values.put(KEY_PATTERN, stringify(pattern));
			entity_values.put(KEY_NOTIFY_COUNT, 0);

			created_id = db.insertOrThrow(TABLE, null, entity_values);

		} catch (Exception tr) {
			Ln.d(tr, "Create entity failed.");
		} finally {
			if (db != null)
				db.close();
		}

		// Get an entity instance to represent this new entity
		Entity e = entity_provider.get();
		e.entityid(created_id);

		Bundle created_extras = e.getExtras();
		created_extras.putString(EXTRA_CACHE_KEY_NAME, name);
		created_extras.putString(EXTRA_CACHE_KEY_KIND, type);
		created_extras.putString(EXTRA_CACHE_KEY_PATTERN, pattern.toString());
		created_extras.putInt(EXTRA_CACHE_KEY_NOTIFY_COUNT, 0);

		// TODO error handling
		return e;
	}

	public void delete(Entity entity) {
		// First open a connection to the database
		SQLiteDatabase db = _db.getWritableDatabase();
		// Attempt the deletion of all relevant records
		try {

			// TODO Don't forget about associated lookups

			// Perform removal
			int deleteCount = db.delete(TABLE, KEY_ID + " == ?",
					new String[] { entity.entityid().toString() });

			// Check if we actually removed a row
			if (deleteCount == 0) {
				// Log.d(DEBUG_TAG, "Delete contact failed.", db.de);
			}

		} catch (Exception tr) {
			Ln.e(tr, "Delete contact failed.");
		} finally {
			if (db != null)
				db.close();
		}
	}

	@Override
	public Entity get(Long id) {
		Cursor c = getCursor(id);

		try {
			if (!c.moveToFirst()) {
				Ln.d("Could not find entity %d", id);
				return null;
			}

			return fromCursor(c);

		} finally {
			c.close();
		}
	}

	private Cursor getCursor(Long id) {
		Cursor c;
		// Open a connection to the database
		SQLiteDatabase sql_db = _db.getReadableDatabase();
		try {
			// Grab all contacts
			c = sql_db.query(TABLE, new String[] { KEY_ID, KEY_NAME, KEY_KIND,
					KEY_PATTERN, KEY_NOTIFY_COUNT }, KEY_ID + " = ?",
					new String[] { id.toString() }, null, null, null);

			if (c.isClosed())
				Ln.d("Opening Cursor failed");

		} finally {
			if (sql_db != null)
				sql_db.close();
		}
		return c;
	}

	@Override
	public Entity fromCursor(Cursor c) {

		Entity e = entity_provider.get();
		e.entityid(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));

		Bundle got_extras = e.getExtras();
		// TODO: Only pull extras if they exist
		got_extras.putString(EXTRA_CACHE_KEY_NAME,
				c.getString(c.getColumnIndexOrThrow(KEY_NAME)));
		got_extras.putString(EXTRA_CACHE_KEY_KIND,
				c.getString(c.getColumnIndexOrThrow(KEY_KIND)));
		got_extras.putString(EXTRA_CACHE_KEY_PATTERN,
				c.getString(c.getColumnIndexOrThrow(KEY_PATTERN)));
		got_extras.putInt(EXTRA_CACHE_KEY_NOTIFY_COUNT,
				c.getInt(c.getColumnIndexOrThrow(KEY_NOTIFY_COUNT)));

		// TODO error handling
		return e;

		// old way
		// return get(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
	}

	@Override
	public Cursor getAll() {
		// Open a connection to the database
		SQLiteDatabase db = _db.getReadableDatabase();
		try {
			Cursor c = db.query(TABLE, null, null, null, null, null, KEY_NAME);

			if (c.isClosed())
				Ln.d("Opening Cursor failed");

			return c;
		} finally {
			if (db != null)
				db.close();
		}
	}

	@Override
	public Cursor getAll(String type) {
		// Open a connection to the database
		SQLiteDatabase db = _db.getReadableDatabase();
		try {
			// Grab all contacts
			// return db.query(TABLE,
			// null, //new String[] { KEY_ID },
			// null, null, null, null, null, null);
			// TODO Fix
			Cursor c = db.query(TABLE, null, KEY_KIND + "= ?",
					new String[] { type }, null, null, KEY_NAME);

			if (c.isClosed())
				Ln.d("Opening Cursor failed");

			return c;
		} finally {
			if (db != null)
				db.close();
		}
	}

	public String getDisplayName(Entity entity) {
		// update cache first if needed
		if (!entity.getExtras().containsKey(EXTRA_CACHE_KEY_NAME))
			entity = get(entity.entityid());

		return entity.getExtras().getString(EXTRA_CACHE_KEY_NAME);
	}

	static class Helper implements IDatabaseHelper {
		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create entity table
			String entity_sql = "CREATE TABLE " + TABLE + " ( " + KEY_ID
					+ " INTEGER PRIMARY KEY, " + KEY_KIND + " string, "
					+ KEY_NAME + " string, " + KEY_PATTERN + " string, "
					+ KEY_NOTIFY_COUNT + " integer " + ");";
			db.execSQL(entity_sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

		@Override
		public int version() {
			return VERSION;
		}

	}

	@Override
	public void setPattern(Entity entity, long[] pattern) {
		// Open a connection to the database
		SQLiteDatabase db = _db.getReadableDatabase();
		try {
			String stringified_pattern = stringify(pattern);

			ContentValues values = new ContentValues(1);
			values.put(KEY_PATTERN, stringified_pattern);

			db.update(TABLE, values, KEY_ID + " = ?", new String[] { entity
					.entityid().toString() });

			// update cache as well
			entity.getExtras().putString(EXTRA_CACHE_KEY_PATTERN,
					stringified_pattern);

		} finally {
			if (db != null)
				db.close();
		}
	}

	@Override
	public long[] getPattern(Entity entity) {
		// update cache first if needed
		if (!entity.getExtras().containsKey(EXTRA_CACHE_KEY_PATTERN))
			entity = get(entity.entityid());

		// Unpack pattern from database
		// should be a comma separated list of longs
		String pattern_packed = entity.getExtras().getString(
				EXTRA_CACHE_KEY_PATTERN);
		if (pattern_packed == null)
			return null;
		String[] pattern_parts = pattern_packed.split(",");
		long[] pattern = new long[pattern_parts.length];

		try {
			for (int i = 0; i < pattern.length; i++)
				pattern[i] = Long.parseLong(pattern_parts[i].trim());
		} catch (Exception e) {
			Ln.d(e, "Could not parse pattern '%s' from database.",
					pattern_packed);
		}

		return pattern;
	}

	private String stringify(long[] pattern) {
		if (pattern == null)
			return "";

		String pattern_str = Arrays.toString(pattern);

		return pattern_str.substring(1, pattern_str.length() - 1);
	}

	@Override
	public String getKind(Entity entity) {
		// update cache first if needed
		if (!entity.getExtras().containsKey(EXTRA_CACHE_KEY_KIND))
			entity = get(entity.entityid());

		return entity.getExtras().getString(EXTRA_CACHE_KEY_KIND);
	}
}
