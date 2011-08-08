package com.danielstiner.vibrates.database;

import roboguice.util.Ln;

import com.danielstiner.vibrates.Entity;
import com.google.inject.Inject;
import com.google.inject.Provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
	
	//protected static final String DEFAULT_KIND = "";

	@Inject private IDatabase db;

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
		if(identifier == null)
			return null;
		
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
    				new String[] { KEY_ENTITYID },
    				KEY_IDENTIFIER + " = ?",
    				new String[] { identifier },
    				null, null, null, null);
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
        	Cursor c = sql_db.query(
    				TABLE,
    				null,
    				KEY_ENTITYID + " = ? AND " + KEY_KIND + " = ?",
    				new String[] { owner.entityid().toString(), kind },
    				null, null, null, null);
        	int test = c.getCount();
        	return c;
        	
        } finally {
            if (sql_db != null)
            	sql_db.close();
        }
	}

	@Override
	public void add(Entity owner, String identifier, String kind) {
		if(identifier == null) {
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
	public String identifierFromCursor(Cursor c) {
		return c.getString(c.getColumnIndexOrThrow(KEY_IDENTIFIER));
	}

	@Override
	public String kindFromCursor(Cursor c) {
		return c.getString(c.getColumnIndexOrThrow(KEY_KIND));
	}
	
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
			+ KEY_ID + " INTEGER PRIMARY KEY, "
			+ KEY_IDENTIFIER + " string KEY, "
			+ KEY_ENTITYID + " integer, "
			+ KEY_KIND + " string "
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



	@Override
	public void update(Entity entity) {
		// TODO Auto-generated method stub
		
	}
	
//	private void updatePhoneNumbers(Contact contact, ContentResolver cr) {
//	Cursor c = cr.query(
//		ContactsContract.Data.CONTENT_URI,
//		new String[] {
//			ContactsContract.Data._ID,
//			ContactsContract.CommonDataKinds.Phone.NUMBER,
//			ContactsContract.CommonDataKinds.Phone.TYPE,
//			ContactsContract.CommonDataKinds.Phone.LABEL
//			},
//		ContactsContract.Data.CONTACT_ID + " = ?" + " AND "
//		+ ContactsContract.Data.MIMETYPE + " = ?",
//		new String[] {
//			contact.getId().toString(),
//			ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
//			},
//		null
//		);
//	
//	if(c.getCount() < 1) return;
//	
//	// Open a connection to the database
//	SQLiteDatabase db = _dbHelper.getReadableDatabase();
//	
//	c.moveToFirst();
//	
//	while(!c.isAfterLast()) {
//		// FIXME: Some fancy parsing of this number?
//		String number = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
//		
//		try {
//		
//			Cursor vibrates_cursor = db.query(
//					TABLE_VIBRATES,
//					new String[]{KEY_ROWID},
//					KEY_CONTACT_ID + " == ? AND "
//					+ KEY_IDENTIFIER + " == ?",
//					new String[]{contact.getId().toString(), number},
//					null,
//					null,
//					null,
//					"1");
//			
//			if(vibrates_cursor.getCount() < 1) {
//				// Need to add this new phone number
//				// Prep data for insertion
//	            ContentValues values = new ContentValues(3);
//	            values.put(KEY_CONTACT_ID, contact.getId());
//	            values.put(KEY_IDENTIFIER, contact.getLookupKey());
//	            values.put(KEY_PATTERN, PATTERN_DEFAULT);
//	 
//	            // Perform insert
//	            long insertId = db.insertOrThrow(TABLE_VIBRATES, null, values);
//	            
//	            // Check if we actually inserted a row
//	            //if( insertId == -1 )
//	            	// TODO bad stuff
//		        
//			}
//		
//		} finally {
//            if (db != null)
//                db.close();
//        }
//		//c.getInt(c.getColumnIndexOrThrow(ContactsContract.Data._ID))
//		//c.getInt(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE))
//		//c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL))
//		c.moveToNext();
//	} // while(!c.isAfterLast())
//}

}
