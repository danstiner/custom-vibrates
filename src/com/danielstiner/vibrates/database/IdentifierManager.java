package com.danielstiner.vibrates.database;

import roboguice.util.Ln;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.danielstiner.vibrates.Entity;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class IdentifierManager implements IIdentifierManager {

	private static final int VERSION = Database.VERSION;

	protected static final String TABLE = "lookups";

	protected static final String KEY_ID = "_id";
	protected static final String KEY_KIND = "kind";
	protected static final String KEY_ENTITYID = "entity";
	protected static final String KEY_IDENTIFIER = "identifier";

	public static final String KIND_DEFAULT = "unknown";
	public static final String KIND_CONTACTS_CONTRACT_ID = "generated.contacts_contract_id";
	public static final String KIND_CONTACTS_CONTRACT_NAME = "generated.contacts_contract_name";
	public static final String KIND_CONTACTS_CONTRACT_LOOKUP = "generated.contacts_contract_lookup";

	public static final String KIND_CONTACTS_GROUP_ID = "generated.contacts_group_id";
	public static final String KIND_CONTACTS_GROUP_TITLE = "generated.contacts_group_title";
	// protected static final String DEFAULT_KIND = "";

	@Inject
	private IDatabase db;

	private Provider<Entity> entity_provider;

	@Inject
	public IdentifierManager(Provider<Entity> entity_provider) {
		this.entity_provider = entity_provider;
	}

	@Override
	public Cursor getOrphans() {
		return getByEntityId(new Long(Entity.ID_NOBODY));
	}

	@Override
	public Cursor get(Entity owner) {
		if (owner == null)
			return null;
		else
			return getByEntityId(owner.entityid());
	}

	private Cursor getByEntityId(Long entityid) {
		// Open a connection to the database
		SQLiteDatabase sql_db = db.getReadableDatabase();
		try {
			// Grab all contacts
			return sql_db.query(TABLE, null, KEY_ENTITYID + " = ?",
					new String[] { entityid.toString() }, null, null, null);
		} finally {
			if (sql_db != null)
				sql_db.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.danielstiner.vibrates.IIdentifierManager#get(java.lang.String)
	 */
	public Cursor get(String identifier) {
		if (identifier == null)
			return null;

		return getByEntityIdentifier(identifier);

		// Cursor matches = getByEntityIdentifier(identifier);
		// // Make sure we only have one entity match
		// if(matches.getCount() > 1)
		// {
		// Ln.d("Found multiple (%d) entities for the identifier: %s",
		// matches.getCount(), identifier);
		// //return null;
		// }
		// if(!matches.moveToFirst())
		// return null;
		// Long entityid =
		// matches.getLong(matches.getColumnIndexOrThrow(KEY_ENTITYID));
		// // Must have worked
		// return entity_provider.get().entityid(entityid);
	}

	public Cursor get(String identifier, String kind) {
		if (identifier == null || kind == null)
			return null;

		return getByEntityIdentifierAndKind(identifier, kind);

		// Cursor matches = getByEntityIdentifier(identifier);
		// // Make sure we only have one entity match
		// if(matches.getCount() > 1)
		// {
		// Ln.d("Found multiple (%d) entities for the identifier: %s",
		// matches.getCount(), identifier);
		// //return null;
		// }
		// if(!matches.moveToFirst())
		// return null;
		// Long entityid =
		// matches.getLong(matches.getColumnIndexOrThrow(KEY_ENTITYID));
		// // Must have worked
		// return entity_provider.get().entityid(entityid);
	}

	private Cursor getByEntityIdentifier(String identifier) {
		// Open a connection to the database
		SQLiteDatabase sql_db = db.getReadableDatabase();
		try {
			// Grab all contacts
			Cursor c = sql_db.query(TABLE, null, KEY_IDENTIFIER + " = ?",
					new String[] { identifier }, null, null, null, null);
			int test = c.getCount();
			return c;
		} finally {
			if (sql_db != null)
				sql_db.close();
		}
	}

	private Cursor getByEntityIdentifierAndKind(String identifier, String kind) {
		// Open a connection to the database
		SQLiteDatabase sql_db = db.getReadableDatabase();
		try {
			// Grab all contacts
			Cursor c = sql_db.query(TABLE, null, KEY_IDENTIFIER + " = ?"
					+ " AND " + KEY_KIND + " = ? ", new String[] { identifier,
					kind }, null, null, null, null);
			int test = c.getCount();
			return c;
		} finally {
			if (sql_db != null)
				sql_db.close();
		}
	}

	@Override
	public Cursor get(Entity owner, String kind) {
		// Open a connection to the database
		SQLiteDatabase sql_db = db.getReadableDatabase();
		try {
			// Grab all matching lookups
			Cursor c = sql_db
					.query(TABLE, null, KEY_ENTITYID + " = ? AND " + KEY_KIND
							+ " = ?", new String[] {
							owner.entityid().toString(), kind }, null, null,
							null, null);
			int test = c.getCount();
			return c;

		} finally {
			if (sql_db != null)
				sql_db.close();
		}
	}

	@Override
	public void add(Entity owner, String identifier, String kind) {
		if (identifier == null) {
			Ln.e("Will not add a null identifier");
			return;
		}
		Long owner_id = (owner == null) ? Entity.ID_NOBODY : owner.entityid();
		// Open a connection to the database
		SQLiteDatabase sql_db = db.getWritableDatabase();
		try {
			ContentValues ident_values = new ContentValues(3);
			ident_values.put(KEY_ENTITYID, owner_id.toString());
			ident_values.put(KEY_IDENTIFIER, identifier);
			ident_values.put(KEY_KIND, kind);
			// TODO: throw is temporary
			long identifierId = sql_db.insertOrThrow(TABLE, null, ident_values);
		} finally {
			if (sql_db != null)
				sql_db.close();
		}
	}

	@Override
	public Entity entityFromCursor(Cursor c) {
		Long entityid = c.getLong(c.getColumnIndexOrThrow(KEY_ENTITYID));
		// Must have worked
		return entity_provider.get().entityid(entityid);
	}

	@Override
	public String identifierFromCursor(Cursor c) {
		return c.getString(c.getColumnIndexOrThrow(KEY_IDENTIFIER));
	}

	@Override
	public String kindFromCursor(Cursor c) {
		return c.getString(c.getColumnIndexOrThrow(KEY_KIND));
	}

	// @Override
	// public Entity entityFromCursor(Cursor c) {
	// return new Entity(
	// c.getString(c.getColumnIndexOrThrow(KEY_IDENTIFIER)),
	// c.getLong(c.getColumnIndexOrThrow(KEY_ENTITYID))
	// );
	// }

	@Override
	public int removeAll(Entity entity) {
		if (entity == null || entity.entityid() == null) {
			Ln.e("Cannot remove identifiers for a null entity.");
			return 0;
		}
		// Open a connection to the database
		SQLiteDatabase sql_db = db.getWritableDatabase();
		try {
			// Try and remove any identifiers for this entity
			int delete_count = sql_db.delete(TABLE, KEY_ENTITYID + " = ?",
					new String[] { entity.entityid().toString() });
			return delete_count;
		} finally {
			if (sql_db != null)
				sql_db.close();
		}
	}

	@Override
	public int removeAll(Entity entity, String kind) {
		if (entity == null || entity.entityid() == null) {
			Ln.e("Cannot remove identifiers for a null entity.");
			return 0;
		}
		// Open a connection to the database
		SQLiteDatabase sql_db = db.getWritableDatabase();
		try {
			// Try and remove any identifiers for this entity
			int delete_count = sql_db.delete(TABLE, KEY_ENTITYID + " = ? AND "
					+ KEY_KIND + " = ? ", new String[] {
					entity.entityid().toString(), kind });
			return delete_count;
		} finally {
			if (sql_db != null)
				sql_db.close();
		}
	}

	static class Helper implements IDatabaseHelper {

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create lookup table
			String lookup_sql = "CREATE TABLE " + TABLE + " ( " + KEY_ID
					+ " INTEGER PRIMARY KEY, " + KEY_IDENTIFIER
					+ " string KEY, " + KEY_ENTITYID + " integer, " + KEY_KIND
					+ " string " + ");";
			db.execSQL(lookup_sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

		@Override
		public int version() {
			return VERSION;
		}

	}
}
