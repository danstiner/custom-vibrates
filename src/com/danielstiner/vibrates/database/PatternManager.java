package com.danielstiner.vibrates.database;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.managers.Manager;
import com.danielstiner.vibrates.utility.MorseCodePattern;

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

public class PatternManager implements IPatternManager {
	
	private static final String PREFIX = "com.danielstiner.vibrates.manager";
	
	public static final String ENTITY_ID_KEY = PREFIX + ".id";
	
	private static final String DEBUG_TAG = "VibratesEntityManager";
	
	/** Identifies patterns/lookups not attached to a contact (yet parent-less) */
	public static final int ID_NOBODY = -3;

	private Context _context;
		 
    public PatternManager(Context c) {
    	super(c);
    	_context = c;
    }
    protected PatternManager(Manager m) {
    	super(m);
    }
    
    public static long[] generate(String text) {
		// TODO Auto-generated method stub
		return MorseCodePattern.morsify(text);
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
            		new String[] {entity.entityid().toString()}
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
    			new IdentifierManager((Manager)super.clone()).add(null, identifier);
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
	
	

	public long[] get(Entity entity, String type) {
		// TODO Auto-generated method stub
		return null;
	}
	private static boolean isValidPattern(long[] pattern) {
		if(pattern == null) return false;
		// TODO Auto-generated method stub
		return true;
	}
    
}
