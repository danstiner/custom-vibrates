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
	
	private static final int VERSION = VibratesDatabase.VERSION;
	
	protected static final String TABLE = "entities";
	
	protected static final String KEY_ROWID = "_id";
	protected static final String KEY_ID = "id";
	protected static final String KEY_KIND = "kind";
	protected static final String KEY_NAME = "name";
	protected static final String KEY_PATTERN = "pattern";
	protected static final String KEY_TIMES_CONTACTED = "times";

	
	/** Identifies patterns/lookups not attached to a contact (yet parent-less) */
	public static final int ID_NOBODY = -3;

	private Context _context;

	private Provider<IdentifierManager> identifiermanager_provider;
	
	private IDatabase _db;

	private Provider<Entity> entity_provider;
	
	@Inject
    public EntityManager(IDatabase db, Provider<Entity> entity_provider) {
    	this._db = db;
    	this.entity_provider = entity_provider;
    }
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static Uri getLookupUri(Entity c) {
		// FIXME: Don't assume the identier is the contact lookup, or the id even
		return ContactsContract.Contacts.getLookupUri(c.entityid(), c.identifier());
	}
	
	@Inject
    public EntityManager(IDatabase db, Context c, Provider<IdentifierManager> identifiermanager_provider) {
		this._db  = db;
    	this._context = c;
    	this.identifiermanager_provider = identifiermanager_provider;
    }
    
    
    
    public Entity create(String identifier) {
    	return create(identifier, null);
    }
    public Entity create(String identifier, String name) {
    	return create(identifier, name, null);
    }
    public Entity create(String identifier, String name, long[] pattern) {
    	return create(identifier, name, pattern, null);
    }
    public Entity create(String identifier, String name, long[] pattern, String type) {
    	return create(identifier, name, pattern, type, 0);
    }
    public Entity create(String identifier, String name, long[] pattern, String type, int times_contacted) {
    	// First first see if such already exists
    	Entity entity = get(identifier);
    	if(entity != null) return entity;
    	
    	// TODO way more error checking on the parameters
    	
    	// First open a connection to the database
    	SQLiteDatabase db = _db.getWritableDatabase();
    	try {
            // Perform insert of actual entity
            ContentValues entity_values = new ContentValues(3);
            entity_values.put(KEY_NAME, name);
            entity_values.put(KEY_KIND, type);
            entity_values.put(KEY_PATTERN, pattern.toString());
            // TODO: throw is temporary
            long entityId = db.insertOrThrow(TABLE, null, entity_values);
            
            // TODO: Check insertion here
            
            // Now map a lookup for the given identifier
            identifiermanager_provider.get().add(entity, identifier);
//            ContentValues lookup_values = new ContentValues(2);
//            lookup_values.put(KEY_LOOKUP_IDENTIFIER, identifier);
//            lookup_values.put(KEY_LOOKUP_ENTITYID, entityId);
//            long lookupId = db.insertOrThrow(TABLE_LOOKUP, null, lookup_values);
            
            // Must have worked
            return get(entityId);
            
    	} catch(Exception tr) {
            Ln.d(tr, "Add entity failed.");
        } finally {
            if (db != null)
                db.close();
        }
        
        // Must not have worked, maybe there was an entry in the db already?
        return null;
	}
    public Entity createFromContactUri(Uri contact_uri) {
    	// Example uri: content://com.android.contacts/contacts/lookup/0r7-2C46324E483C324A3A484634/7
		Log.v("Got a result: %s", contact_uri.toString());
		// get the contact id from the Uri
		Long origId = Long.parseLong(contact_uri.getLastPathSegment());
		
		// get the permanent lookup key in case the id changes
		List<String> resultSegments = contact_uri.getPathSegments();
		// hint, its the second to last segment
		String origLookupKey = resultSegments.get(resultSegments.size()-2);
		
		// TODO fix identifier assumptions
		// Grab some info from the system contact service
		Cursor c = _context.getContentResolver().query(
				ContactsContract.Contacts.getLookupUri(origId, origLookupKey),
				new String[] {
					ContactsContract.Contacts.DISPLAY_NAME,
					ContactsContract.Contacts.LOOKUP_KEY,
					ContactsContract.Contacts.TIMES_CONTACTED
					},
				null,
				null,
				null);
		try {
		    c.moveToFirst();
		    // Grab some info
		    String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		    String lookup = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
		    int times_contacted = c.getInt(c.getColumnIndexOrThrow(ContactsContract.Contacts.TIMES_CONTACTED));
		    
		    // Generate a default contact pattern
		    long[] pattern = PatternManager.generate(name);
		    
		    return create(lookup, name, pattern, Entity.TYPE_CONTACTSCONTRACTCONTACT, times_contacted);
		    
		} finally {
		    c.close();
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
	
	public Entity get(Long id) {
		return null;
	}
	public Entity get(String identifier) {
		// First open a connection to the database
    	SQLiteDatabase db = getReadableDatabase();
    	// Then attempt a search
    	try {
    		Cursor c = makeIdentifierManager().get(identifier);
    		// No matches? Log it for the user to deal with
    		if(c.getCount() < 1) {
    			makeIdentifierManager().add(null, identifier);
    			return null;
    		} else if (c.getCount() > 1) {
    			// Multiple matches, also bad
    			// TODO log it
    			return null;
    		}
    		
    		// We have enough information to init an entity
    		c.moveToFirst();
    		return IdentifierManager.entityFromCursor(c);
    		
    	} catch(Exception tr) {
            Log.d(DEBUG_TAG, "Search for entity failed.", tr);
        } finally {
            if (db != null)
                db.close();
        }
        return null;
	}
	
	/**
	 * Gives a photo for an entity if at all possible, otherwise returns null
	 * @param entity Entity instance to grab a photostream for
	 * @return
	 */
	public InputStream getPhotoStream(Entity entity) {
		// FIXME: Hmm, more complicated, we need to change this depending on the entity type
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, entity.entityid());
        InputStream input = Contacts.openContactPhotoInputStream(_context.getContentResolver(), uri);
		return input;
	}
	
	public Entity fromCursor(Cursor c) {
		return get(c.getLong(c.getColumnIndexOrThrow(com.danielstiner.vibrates.managers.KEY_ENTITY_ID)));
	}
	
	public Cursor getAll() {
		// Open a connection to the database
    	SQLiteDatabase db = getReadableDatabase();
        try {
        	// Grab all contacts 
        	return db.query(TABLE_ENTITIES, null, null, null, null, null, null);
        } finally {
            if (db != null)
                db.close();
        }
	}
	
	
	public void update(Entity entity) {
		ContentResolver cr = _context.getContentResolver();
		// FIXME
		// TODO
		// Pull data from system contact service
		
		//updatePhoneNumbers(contact, cr);

	}
	
//	private void updatePhoneNumbers(Contact contact, ContentResolver cr) {
//		Cursor c = cr.query(
//			ContactsContract.Data.CONTENT_URI,
//			new String[] {
//				ContactsContract.Data._ID,
//				ContactsContract.CommonDataKinds.Phone.NUMBER,
//				ContactsContract.CommonDataKinds.Phone.TYPE,
//				ContactsContract.CommonDataKinds.Phone.LABEL
//				},
//			ContactsContract.Data.CONTACT_ID + " = ?" + " AND "
//			+ ContactsContract.Data.MIMETYPE + " = ?",
//			new String[] {
//				contact.getId().toString(),
//				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
//				},
//			null
//			);
//		
//		if(c.getCount() < 1) return;
//		
//		// Open a connection to the database
//    	SQLiteDatabase db = _dbHelper.getReadableDatabase();
//		
//		c.moveToFirst();
//		
//		while(!c.isAfterLast()) {
//			// FIXME: Some fancy parsing of this number?
//			String number = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//			
//			try {
//			
//				Cursor vibrates_cursor = db.query(
//						TABLE_VIBRATES,
//						new String[]{KEY_ROWID},
//						KEY_CONTACT_ID + " == ? AND "
//						+ KEY_IDENTIFIER + " == ?",
//						new String[]{contact.getId().toString(), number},
//						null,
//						null,
//						null,
//						"1");
//				
//				if(vibrates_cursor.getCount() < 1) {
//					// Need to add this new phone number
//					// Prep data for insertion
//		            ContentValues values = new ContentValues(3);
//		            values.put(KEY_CONTACT_ID, contact.getId());
//		            values.put(KEY_IDENTIFIER, contact.getLookupKey());
//		            values.put(KEY_PATTERN, PATTERN_DEFAULT);
//		 
//		            // Perform insert
//		            long insertId = db.insertOrThrow(TABLE_VIBRATES, null, values);
//		            
//		            // Check if we actually inserted a row
//		            //if( insertId == -1 )
//		            	// TODO bad stuff
//			        
//				}
//			
//			} finally {
//	            if (db != null)
//	                db.close();
//	        }
//			//c.getInt(c.getColumnIndexOrThrow(ContactsContract.Data._ID))
//			//c.getInt(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE))
//			//c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL))
//			c.moveToNext();
//		} // while(!c.isAfterLast())
//	}
	
	public String getDisplayName(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public long[] getPattern(Entity entity) {
		return getPattern(entity, null);
	}
	public long[] getPattern(Entity entity, String type)
	{
		try {
			return makePatternManager().get(entity, type);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	static class Helper implements IDatabaseHelper
	{
		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create entity table
			String entity_sql = "CREATE TABLE "
			+ TABLE + " ( "
			+ KEY_ROWID + " integer PRIMARY KEY AUTOINCREMENT, "
			+ KEY_ID + " integer KEY AUTOINCREMENT, "
			+ KEY_KIND + " string, "
			+ KEY_NAME + " string, "
			+ KEY_PATTERN + " string, "
			//+ KEY_TIMES_CONTACTED + " integer "
			+ ");";
			db.execSQL(entity_sql);
		}
	
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public int version() {
			return VERSION;
		}
	
	}
	
}
