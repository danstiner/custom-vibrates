package com.danielstiner.vibrates;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class EntityManager extends Manager {
	
	private static final String PREFIX = "com.danielstiner.vibrates.manager";
	
	public static final String ENTITY_ID_KEY = PREFIX + ".id";
	
	private static final String DEBUG_TAG = "VibratesEntityManager";
	
	/** Identifies patterns/lookups not attached to a contact (yet parent-less) */
	public static final int ID_NOBODY = -3;

	private Context _context;
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static Uri getLookupUri(Entity c) {
		// FIXME: Don't assume the identier is the contact lookup, or the id even
		return ContactsContract.Contacts.getLookupUri(c.getId(), c.getIdentifier());
	}
		 
    public EntityManager(Context c) {
    	super(c);
    	_context = c;
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
    	SQLiteDatabase db = getWritableDatabase();
    	try {
            // Perform insert of actual entity
            ContentValues entity_values = new ContentValues(3);
            entity_values.put(KEY_ENTITY_NAME, name);
            entity_values.put(KEY_ENTITY_KIND, type);
            entity_values.put(KEY_ENTITY_PATTERN, pattern.toString());
            // TODO: throw is temporary
            long entityId = db.insertOrThrow(TABLE_ENTITIES, null, entity_values);
            
            // TODO: Check insertion here
            
            // Now map a lookup for the given identifier
            ContentValues lookup_values = new ContentValues(2);
            lookup_values.put(KEY_LOOKUP_IDENTIFIER, identifier);
            lookup_values.put(KEY_LOOKUP_ENTITYID, entityId);
            long lookupId = db.insertOrThrow(TABLE_LOOKUP, null, lookup_values);
            
            // TODO: Check lookup here and rollback entity insert if neccessary
            
            // Must have worked
            return get(entityId);
            
    	} catch(Exception tr) {
            Log.d(DEBUG_TAG, "Add entity failed.", tr);
        } finally {
            if (db != null)
                db.close();
        }
        
        // Must not have worked, maybe there was an entry in the db already?
        return null;
	}
    public Entity createFromContactUri(Uri contact_uri) {
    	// Example uri: content://com.android.contacts/contacts/lookup/0r7-2C46324E483C324A3A484634/7
		Log.v(DEBUG_TAG, "Got a result: " + contact_uri.toString());
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
		    long[] pattern = generatePattern(name);
		    
		    return create(lookup, name, pattern, Entity.TYPE_CONTACTSCONTRACTCONTACT, times_contacted);
		    
		} finally {
		    c.close();
		}
		
    }
    private long[] generatePattern(String name) {
		// TODO Auto-generated method stub
		return MorseCodePattern.morsify(name);
	}


	/**
     * Adds a new pattern or replaces the existing one to a contact.
     * @param mimetype
     * @param identifier
     * @param to If a no contact is given, pattern will be associated with CONTACT_ID_NOBODY
     * @param pattern A null pattern means to use the contact's default pattern
     */
//    public void addPattern(String mimetype, String identifier, Contact to, long[] pattern) {
//    	String where_clause = KEY_MIMETYPE + " == ? AND " + KEY_IDENTIFIER + " == ?";
//    	String[] where_args = new String[]{mimetype, identifier};
//    	// Check our parameters
//    	if(mimetype == null) throw new IllegalArgumentException("Must give a mimetype");
//    	if(identifier == null) throw new IllegalArgumentException("Must give an identifier");
//    	// First open a connection to the database
//    	SQLiteDatabase db = _dbHelper.getWritableDatabase();
//    	try {
//    		// Check if record already exists for this mimetype/identifier
//    		Cursor existing = db.query(
//    				TABLE_VIBRATES,
//    				new String[]{KEY_CONTACT_ID},
//    				where_clause,
//    				where_args,
//    				null,
//    				null,
//    				null,
//    				"1");
//    		
//    		// Prep data for insertion
//            ContentValues values = new ContentValues(4);
//            values.put(KEY_CONTACT_ID, (to != null) ? to.getId() : CONTACT_ID_NOBODY);
//            values.put(KEY_IDENTIFIER, identifier);
//            values.put(KEY_MIMETYPE, mimetype);
//            values.put(KEY_PATTERN, isValidPattern(pattern) ? pattern.toString() : "");
//    		
//    		if(existing.getCount() > 0) {
//    			// Do an update
//    			int updated = db.update(TABLE_VIBRATES, values, where_clause, where_args);
//    			if(updated < 1) throw new Exception("No contact updated.");
//    			if(updated > 1) throw new Exception("Many contacts updated.");
//    		} else {
//    			// Do an insert
//    			db.insertOrThrow(TABLE_VIBRATES, null, values);
//    		}
//            
//    	} catch(Exception tr) {
//            Log.d(DEBUG_TAG, "Update contact failed.", tr);
//        } finally {
//            if (db != null)
//                db.close();
//        }
//    }

	public void remove(Entity entity) {
		// First open a connection to the database
    	SQLiteDatabase db = getWritableDatabase();
    	// Attempt the deletion of all relevant records
    	try {
    		
    		// TODO Don't forget about assocatied lookups
    		
            // Perform removal
            int deleteCount = db.delete(
            		TABLE_ENTITIES,
            		KEY_ENTITY_ID + " == ?",
            		new String[] {entity.getId().toString()}
            		);
            
            // Check if we actually removed a row
            if( deleteCount == 0 ) {
            	//Log.d(DEBUG_TAG, "Delete contact failed.", db.de);
            }
            
    	} catch(Exception tr) {
    		Log.d(DEBUG_TAG, "Delete contact failed.", tr);
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
    		Cursor c = db.query(
				TABLE_LOOKUP,
				new String[]{KEY_LOOKUP_ENTITYID},
				KEY_LOOKUP_IDENTIFIER + " == ?",
				new String[]{identifier},
				null, null, null,
				"2");
    		// No matches? Log it for the user to deal with
    		if(c.getCount() < 1) {
    			addIdentifier(null, identifier);
    			return null;
    		} else if (c.getCount() > 1) {
    			// Multiple matches, also bad
    			// TODO log it
    			return null;
    		}
    		
    		// We have enough information to init an entity
    		c.moveToFirst();
    		return new Entity(identifier, c.getLong(c.getColumnIndexOrThrow(KEY_LOOKUP_ENTITYID)));
    		
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
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, entity.getId());
        InputStream input = Contacts.openContactPhotoInputStream(_context.getContentResolver(), uri);
		return input;
	}
	
	public Entity fromCursor(Cursor c) {
		return get(c.getLong(c.getColumnIndexOrThrow(EntityManager.KEY_ENTITY_ID)));
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
	
	private void addIdentifier(Entity to, String identifier) {
		if(identifier == null)
			throw new IllegalArgumentException("Cannot add a null identifier");
		Long owner_id = to == null ? EntityManager.ID_NOBODY : to.getId();
		// Open a connection to the database
    	SQLiteDatabase db = getReadableDatabase();
        try {
            ContentValues ident_values = new ContentValues(3);
            ident_values.put(KEY_LOOKUP_ENTITYID, owner_id.toString());
            ident_values.put(KEY_LOOKUP_IDENTIFIER, identifier);
            ident_values.put(KEY_LOOKUP_KIND, IdentifierManager.DEFAULT_KIND);
            // TODO: throw is temporary
            long identifierId = db.insertOrThrow(TABLE_LOOKUP, null, ident_values);
        } finally {
            if (db != null)
                db.close();
        }
	}

	public Cursor getIdentifiers(Entity owner)
	{
		Long id = owner == null ? EntityManager.ID_NOBODY : owner.getId();
		// Open a connection to the database
    	SQLiteDatabase db = getReadableDatabase();
        try {
        	// Grab all contacts 
        	return db.query(
        			TABLE_LOOKUP,
        			null,
        			KEY_LOOKUP_ENTITYID + " = ?",
        			new String[]{ id.toString() },
        			null,
        			null,
        			null
        			);
        } finally {
            if (db != null)
                db.close();
        }
	}
	
	public class CCMHelper extends SQLiteOpenHelper {

		public CCMHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// Create entity table
			String entity_sql = "CREATE TABLE "
			+ TABLE_ENTITIES + " ( "
			+ KEY_ENTITY_ROWID + " integer PRIMARY KEY AUTOINCREMENT, "
			+ KEY_ENTITY_ID + " integer KEY AUTOINCREMENT, "
			+ KEY_ENTITY_KIND + " string, "
			+ KEY_ENTITY_NAME + " string, "
			+ KEY_ENTITY_PATTERN + " string, "
			+ KEY_ENTITY_TIMES_CONTACTED + " integer "
			+ ");";
			db.execSQL(entity_sql);
			
			// Create lookup table
			String lookup_sql = "CREATE TABLE "
			+ TABLE_LOOKUP + " ( "
			+ KEY_LOOKUP_ROWID + " integer PRIMARY KEY AUTOINCREMENT, "
			+ KEY_LOOKUP_IDENTIFIER + " string KEY, "
			+ KEY_LOOKUP_ENTITYID + " integer, "
			+ KEY_LOOKUP_KIND + " string, "
			+ ");";
			db.execSQL(lookup_sql);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

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
	
	private boolean isValidPattern(long[] pattern) {
		if(pattern == null) return false;
		// TODO Auto-generated method stub
		return true;
	}
	
	public long[] getPattern(Entity entity) {
		return getPattern(entity, null);
	}
	public long[] getPattern(Entity entity, String type) {
		// TODO Auto-generated method stub
		return null;
	}
	public String getDisplayName(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
