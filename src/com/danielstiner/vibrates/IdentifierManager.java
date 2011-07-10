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

public class IdentifierManager extends Manager {
	
	private static final String PREFIX = "com.danielstiner.vibrates.identifiermanager.";
	
	public static final String ENTITY_ID_KEY = PREFIX + "id";
	
	private static final String DEBUG_TAG = "VibratesIdentM";
	
	public static final String DEFAULT_KIND = Notification.DEFAULT;
	
	protected static final String KEY_KIND = KEY_LOOKUP_KIND;
	protected static final String KEY_ENTITYID = KEY_LOOKUP_ENTITYID;
	protected static final String KEY_IDENTIFIER = KEY_LOOKUP_IDENTIFIER;

	private Context _context;
	 
    public IdentifierManager(Context c) {
    	super(c);
        _context = c;
    }
    public Cursor getAll()
    {
    	return get(null);
    }
	public Cursor get(Entity owner) {
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
            ident_values.put(KEY_LOOKUP_KIND, DEFAULT_KIND);
            // TODO: throw is temporary
            long identifierId = db.insertOrThrow(TABLE_LOOKUP, null, ident_values);
        } finally {
            if (db != null)
                db.close();
        }
	}

	public static String identifierFromCursor(Cursor c) {
		return c.getString(c.getColumnIndexOrThrow(KEY_LOOKUP_IDENTIFIER));
	}

	public static String kindFromCursor(Cursor c) {
		return c.getString(c.getColumnIndexOrThrow(KEY_LOOKUP_KIND));
	}
	
	public static Entity entityFromCursor(Cursor c) {
		// TODO return c.getString(c.getColumnIndexOrThrow(KEY_LOOKUP_KIND));
		return null;
	}

}
