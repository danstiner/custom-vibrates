package com.danielstiner.vibrates.database;

import roboguice.util.Ln;

import com.danielstiner.vibrates.Entity;
import com.google.inject.Inject;
import com.google.inject.Provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class IdentifierManager implements IIdentifierManager {
	
	private static final int VERSION = VibratesDatabase.VERSION;
	
	protected static final String TABLE = "lookups";
	
	protected static final String KEY_ROWID = "_id";
	protected static final String KEY_KIND = "kind";
	protected static final String KEY_ENTITYID = "entity";
	protected static final String KEY_IDENTIFIER = "identifier";
	
	//protected static final String DEFAULT_KIND = "";

	private VibratesDatabase db;

	private Provider<Entity> entity_provider;
	
	@Inject
    public IdentifierManager(VibratesDatabase db, Provider<Entity> entity_provider) {
    	this.db = db;
    	this.entity_provider = entity_provider;
    }
	
	@Override
	public Cursor getOrphans() {
		return getByEntityId(new Long(EntityManager.ID_NOBODY));
	}

	@Override
	public Cursor get(Entity owner) {
		if(owner == null)
			return null;
		else
			return getByEntityId(owner.entityid());
	}
	private Cursor getByEntityId(Long entityid)
	{
		// Open a connection to the database
    	SQLiteDatabase sql_db = db.getReadableDatabase();
        try {
        	// Grab all contacts 
        	return sql_db.query(
        			TABLE,
        			null,
        			KEY_ENTITYID + " = ?",
        			new String[]{ entityid.toString() },
        			null,
        			null,
        			null
        			);
        } finally {
            if (sql_db != null)
            	sql_db.close();
        }
	}
	/* (non-Javadoc)
	 * @see com.danielstiner.vibrates.IIdentifierManager#get(java.lang.String)
	 */
	public Entity get(String identifier) {
		Cursor matches = getByEntityIdentifier(identifier);
		// Make sure we only have one entity match
		if(matches.getCount() > 1)
		{
			Ln.d("Found multiple (%d) entities for the identifier: %s", matches.getCount(), identifier);
			return null;
		}
		if(matches.getCount() > 1)
			return null;
		if(!matches.moveToFirst())
			return null;
		Long entityid = matches.getLong(matches.getColumnIndexOrThrow(KEY_ENTITYID));
		// Must have worked
		return entity_provider.get().entityid(entityid);
	}
	private Cursor getByEntityIdentifier(String identifier) {
		// Open a connection to the database
    	SQLiteDatabase sql_db = db.getReadableDatabase();
        try {
        	// Grab all contacts 
        	return sql_db.query(
    				TABLE,
    				new String[]{KEY_ENTITYID},
    				KEY_IDENTIFIER + " == ?",
    				new String[]{identifier},
    				null, null, null, null);
        } finally {
            if (sql_db != null)
            	sql_db.close();
        }
	}

	public void add(Entity owner, String identifier) {
		if(identifier == null)
			throw new IllegalArgumentException("Cannot add a null identifier");
		Long owner_id = (owner == null) ? EntityManager.ID_NOBODY : owner.entityid();
		// Open a connection to the database
    	SQLiteDatabase sql_db = db.getWritableDatabase();
        try {
            ContentValues ident_values = new ContentValues();
            ident_values.put(KEY_ENTITYID, owner_id.toString());
            ident_values.put(KEY_IDENTIFIER, identifier);
            //ident_values.put(KEY_KIND, DEFAULT_KIND);
            // TODO: throw is temporary
            long identifierId = sql_db.insertOrThrow(TABLE, null, ident_values);
        } finally {
            if (sql_db != null)
            	sql_db.close();
        }
	}
	
	
	
//	@Override
//	public String identifierFromCursor(Cursor c) {
//		return c.getString(c.getColumnIndexOrThrow(KEY_IDENTIFIER));
//	}

//	@Override
//	public String kindFromCursor(Cursor c) {
//		return c.getString(c.getColumnIndexOrThrow(KEY_KIND));
//	}
	
//	@Override
//	public Entity entityFromCursor(Cursor c) {
//		return new Entity(
//				c.getString(c.getColumnIndexOrThrow(KEY_IDENTIFIER)),
//				c.getLong(c.getColumnIndexOrThrow(KEY_ENTITYID))
//				);
//	}

	
	
	static class Helper implements IDatabaseHelper
	{

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create lookup table
			String lookup_sql = "CREATE TABLE "
			+ TABLE + " ( "
			+ KEY_ROWID + " integer PRIMARY KEY AUTOINCREMENT, "
			+ KEY_IDENTIFIER + " string KEY, "
			+ KEY_ENTITYID + " integer, "
			+ KEY_KIND + " string, "
			+ ");";
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
