package com.danielstiner.vibrates;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;

public class CustomContactManager {
	
	private CCMHelper _dbHelper;
	
	private Context _context;
	
	private static final int version = 1;
	
	private static final String DEBUG_TAG = "CustomVibratesCCM";
	
	public static final String TABLE_CONTACTS = "Contacts";
	public static final String TABLE_VIBRATES = "Vibrates";
	
	public static final String KEY_CONTACT_ROWID  = "_id";
	public static final String KEY_CONTACT_ID     = "contact_id";
	public static final String KEY_CONTACT_LOOKUP = "lookup";
	
	public static final String KEY_VIBRATE_ID          = "_id";
	public static final String KEY_VIBRATE_CONTACT_ID  = "contactid";
	public static final String KEY_VIBRATE_MIMETYPE    = "mimetype";
	public static final String KEY_VIBRATE_IDENTIFIER  = "identifier";
	public static final String KEY_VIBRATE_PATTERN     = "pattern";

	public static final String MIMETYPE_EMAIL = "";

	public static final String MIMETYPE_SMS = "";

	protected static final String MIMETYPE_DEFAULT = "com.danielstiner.vibrates.mimetype.default";
	
	 
    public CustomContactManager(Context c) {
    	
    	CursorFactory factory = null;
        _dbHelper = new CCMHelper(c, "vibrates", factory, version);
        
        // TODO check c for nulls
        _context = c;
    }
 
    /**
     * 
     * @return Contact ID for performing further modifications on
     * @throws ContactException If the contact could not be created
     */
    public int createContact() throws ContactException {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();

            db.insert("Games", "", values);
        } finally {
            if (db != null)
                db.close();
        }
        throw new ContactException("Could not create Contact");
    }
    
    public boolean add(Contact contact) {
    	// First open a connection to the database
    	SQLiteDatabase db = _dbHelper.getWritableDatabase();
    	// Attempt creation of the record
    	try {
    		
    		// TODO Check if record already exists
    		
    		Cursor existing = db.query(
    				TABLE_CONTACTS,
    				new String[]{KEY_CONTACT_ROWID},
    				KEY_CONTACT_ID + " == ? AND " + KEY_CONTACT_LOOKUP + " == ?",
    				new String[]{contact.getId().toString(), contact.getLookupKey()},
    				null,
    				null,
    				null,
    				"1");
    		
    		if(existing.getCount() > 0)
    			return false;
            
            // Prep data for insertion
            ContentValues values = new ContentValues(2);
            values.put(KEY_CONTACT_ID, contact.getId());
            values.put(KEY_CONTACT_LOOKUP, contact.getLookupKey());
 
            // Perform insert
            // FIXME: throw is temporary
            long insertId = db.insertOrThrow(TABLE_CONTACTS, null, values);
            
            // Check if we actually inserted a row
            if( insertId > 0 )
            	return true;
            
    	} catch(Exception tr) {
            Log.d(DEBUG_TAG, "Add contact failed.", tr);
        } finally {
            if (db != null)
                db.close();
        }
        
        // Must not have worked, maybe there was an entry in the db already?
        return false;
	}

	public void remove(Contact contact) {
		// First open a connection to the database
    	SQLiteDatabase db = _dbHelper.getWritableDatabase();
    	// Attempt the deletion of all relevant records
    	try {
    		
            // Perform removal
            int deleteCount = db.delete(
            		TABLE_CONTACTS,
            		"contact_id == ? AND lookup == ?",
            		new String[] {contact.getId().toString(), contact.getLookupKey().toString()}
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
	
	public Contact getContact(Long id, String lookup) {
		return new Contact(lookup, id, this);
	}
	public Contact getContact(String mimetype, String identifier) {
		// First open a connection to the database
    	SQLiteDatabase db = _dbHelper.getReadableDatabase();
    	// Then attempt a search
    	try {
    		Cursor c = db.query(
				TABLE_VIBRATES,
				new String[]{KEY_VIBRATE_CONTACT_ID},
				KEY_VIBRATE_MIMETYPE
				+ " == ? AND "
				+ KEY_VIBRATE_IDENTIFIER
				+ " == ?",
				new String[]{mimetype, identifier},
				null,
				null,
				null,
				"2");
    		
    		// See if we have multiple or no matches
    		if(c.getCount() != 1)
    			return null;
    		
    		// Now we can go lookup the lookup key in our contacts table...
    		c.moveToFirst();
    		long contact_id = c.getLong(c.getColumnIndexOrThrow(KEY_VIBRATE_CONTACT_ID));
    		c = db.query(
				TABLE_CONTACTS,
				new String[]{KEY_CONTACT_ID},
				KEY_CONTACT_ID
				+ " == ?",
				new String[]{new Long(contact_id).toString()},
				null,
				null,
				null,
				"2"
				);
    		
    		// See if we have multiple or no matches
    		if(c.getCount() != 1)
    			return null;
    		
    		// Finally we have enough info to create a contact
    		c.moveToFirst();
    		return new Contact(
    			c.getString(c.getColumnIndexOrThrow(KEY_CONTACT_LOOKUP)),
    			contact_id,
				this
				);
    		
    	} catch(Exception tr) {
            Log.d(DEBUG_TAG, "Search for contact failed.", tr);
        } finally {
            if (db != null)
                db.close();
        }
        return null;
	}
	
	public Map<String, String> getContactInfo(String lookupKey, Long id) {
		// Grab some info from the system contact service
		String[] fields = new String[] {
			ContactsContract.Contacts.DISPLAY_NAME,
			ContactsContract.Contacts.LOOKUP_KEY,
			ContactsContract.Contacts._ID,
			ContactsContract.Contacts.TIMES_CONTACTED,
			ContactsContract.Contacts.LAST_TIME_CONTACTED
			};
		Cursor c = _context.getContentResolver().query(
				ContactsContract.Contacts.getLookupUri(id, lookupKey),
				fields,
				null,
				null,
				null);
		try {
			// Pull out info into a map for use
			Map<String, String> map = new HashMap<String, String>();
		    c.moveToFirst();
		    for(String field : fields)
		    	map.put(field, c.getString(c.getColumnIndexOrThrow(field)));
		    return map;
		    
		} finally {
		    c.close();
		}
	}
	
	
	public InputStream getPhotoStream(long contact_id) {
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contact_id);
        InputStream input = Contacts.openContactPhotoInputStream(_context.getContentResolver(), uri);
		return input;
	}
	
	public Contact getContactFromCursor(Cursor c) {
		
		int contactIdCol = c.getColumnIndex(CustomContactManager.KEY_CONTACT_ID);
		Long contactId = c.getLong(contactIdCol);
		
		int contactLookupCol = c.getColumnIndex(CustomContactManager.KEY_CONTACT_LOOKUP);
		String contactLookup = c.getString(contactLookupCol);
		
		return getContact(contactId, contactLookup);
	}

	public Cursor getServicesAndContacts() {
		Cursor c;
		// Open a connection to the database
    	SQLiteDatabase db = _dbHelper.getReadableDatabase();
        try {
        	// Grab all contacts using the given where clause
        	c = db.query(TABLE_CONTACTS, null, null, null, null, null, null);
        	
        	int ccount = c.getCount();
        	//System.out.print(ccount);
        	//db.query
        	// TODO anything else?
        } finally {
            if (db != null)
                db.close();
        }
        
        
        return c;
	}
	
	
	public class CCMHelper extends SQLiteOpenHelper {

		public CCMHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			// Create contact table
			String contact_sql = "create table " +
			TABLE_CONTACTS + " " +
	        "("+KEY_CONTACT_ROWID+" integer PRIMARY KEY AUTOINCREMENT," +
	        KEY_CONTACT_ID + " integer key, " +
	        KEY_CONTACT_LOOKUP + " string); ";
			db.execSQL(contact_sql);
			
			// Create per service customization table
			String service_sql = "create table " +
			TABLE_VIBRATES + " " +
	        "(" + KEY_VIBRATE_ID + " integer PRIMARY KEY AUTOINCREMENT, " +
	        KEY_VIBRATE_CONTACT_ID + " integer key, " +
	        KEY_VIBRATE_MIMETYPE + " string, " +
	        KEY_VIBRATE_IDENTIFIER + " string, " +
	        KEY_VIBRATE_PATTERN + " string); ";
			db.execSQL(service_sql);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}


	public List<Pair<String,long[]>> getVibrates(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
