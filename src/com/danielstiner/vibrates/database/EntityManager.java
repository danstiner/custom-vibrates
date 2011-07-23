package com.danielstiner.vibrates.database;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.util.Ln;

import com.danielstiner.vibrates.Entity;
import com.google.inject.Inject;
import com.google.inject.Provider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.util.Pair;

public class EntityManager implements IEntityManager {
	
	private static final int VERSION = Database.VERSION;
	
	protected static final String TABLE = "entities";
	
	protected static final String KEY_ID = "_id";
	protected static final String KEY_KIND = "kind";
	protected static final String KEY_NAME = "name";
	protected static final String KEY_PATTERN = "pattern";
	protected static final String KEY_NOTIFY_COUNT = "notified";

	
	/** Identifies patterns/lookups not attached to a contact (yet parent-less) */
	public static final int ID_NOBODY = -3;

	//private Provider<IdentifierManager> identifiermanager_provider;
	
	@Inject private IDatabase _db;

	private Provider<Entity> entity_provider;
	
	private Activity activity;
	
	@Inject
    public EntityManager(Provider<Entity> entity_provider, Activity activity) {
    	this.entity_provider = entity_provider;
    	this.activity = activity;
    }
	
//	/**
//	 * 
//	 * @param c
//	 * @return
//	 */
//	public static Uri getLookupUri(Entity c) {
//		// FIXME: Don't assume the identier is the contact lookup, or the id even
//		return ContactsContract.Contacts.getLookupUri(c.entityid(), c.identifier());
//	}    
    
	@Override
    public Entity create(String name, long[] pattern, String type)
    {
    	// First open a connection to the database
    	SQLiteDatabase db = _db.getWritableDatabase();
    	try {
            // Perform insert of actual entity
            ContentValues entity_values = new ContentValues(3);
            entity_values.put(KEY_NAME, name);
            entity_values.put(KEY_KIND, type);
            entity_values.put(KEY_PATTERN, pattern.toString());
            entity_values.put(KEY_NOTIFY_COUNT, 0);
            
            long entityId = db.insertOrThrow(TABLE, null, entity_values);
            
            // TODO: Check insertion here
            
            // Now map a lookup for the given identifier
            //identifiermanager_provider.get().add(entity, identifier);
//            ContentValues lookup_values = new ContentValues(2);
//            lookup_values.put(KEY_LOOKUP_IDENTIFIER, identifier);
//            lookup_values.put(KEY_LOOKUP_ENTITYID, entityId);
//            long lookupId = db.insertOrThrow(TABLE_LOOKUP, null, lookup_values);
            
            // Must have worked
            return get(entityId);
            
    	} catch(Exception tr) {
            Ln.d(tr, "Create entity failed.");
            return null;
        } finally {
            if (db != null)
                db.close();
        }
    }

	public void remove(Entity entity) {
		// First open a connection to the database
    	SQLiteDatabase db = _db.getWritableDatabase();
    	// Attempt the deletion of all relevant records
    	try {
    		
    		// TODO Don't forget about associated lookups
    		
            // Perform removal
            int deleteCount = db.delete(
            		TABLE,
            		KEY_ID + " == ?",
            		new String[] {entity.entityid().toString()}
            		);
            
            // Check if we actually removed a row
            if( deleteCount == 0 ) {
            	//Log.d(DEBUG_TAG, "Delete contact failed.", db.de);
            }
            
    	} catch(Exception tr) {
    		Ln.e(tr, "Delete contact failed.");
        } finally {
            if (db != null)
                db.close();
        }
	}
	
	@Override
	public Entity get(Long id) {
		return fromCursor(getCursor(id));
	}
	private Cursor getCursor(Long id) {
		// Open a connection to the database
    	SQLiteDatabase sql_db = _db.getReadableDatabase();
        try {
        	// Grab all contacts 
        	return sql_db.query(
        			TABLE,
        			null,
        			KEY_ID + " = ?",
        			new String[]{ id.toString() },
        			null,
        			null,
        			null
        			);
        } finally {
            if (sql_db != null)
            	sql_db.close();
        }
	}
	
	@Override
	public InputStream getPhotoStream(Entity entity) {
		// TODO: Hmm, more complicated, we need to change this depending on the entity type
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, entity.entityid());
        InputStream input = Contacts.openContactPhotoInputStream(activity.getContentResolver(), uri);
		return input;
	}
	
	@Override
	public Entity fromCursor(Cursor c) {
		return get(c.getLong(c.getColumnIndexOrThrow(KEY_ID)));
	}
	
	@Override
	public Cursor getAll() {
		// Open a connection to the database
    	SQLiteDatabase db = _db.getReadableDatabase();
        try {
        	// Grab all contacts 
        	return db.query(TABLE,
        			new String[] { KEY_ID },
        			null, null, null, null, null);
        } finally {
            if (db != null)
                db.close();
        }
	}
	
	public String getDisplayName(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	static class Helper implements IDatabaseHelper
	{
		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create entity table
			String entity_sql = "CREATE TABLE "
			+ TABLE + " ( "
			+ KEY_ID + " INTEGER PRIMARY KEY, "
			+ KEY_KIND + " string, "
			+ KEY_NAME + " string, "
			+ KEY_PATTERN + " string "
			+ KEY_NOTIFY_COUNT + " integer "
			+ ");";
			db.execSQL(entity_sql);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion < 6) {
				Ln.e("Database version %d is far out of date.", oldVersion);
				return;
			}
			if(oldVersion == 6)
				db.execSQL("ALTER TABLE " + TABLE + " ADD COLUMN " + KEY_NOTIFY_COUNT + " INTEGER;");
		}
	
		@Override
		public int version() {
			return VERSION;
		}
	
	}

	@Override
	public void update(Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
}
