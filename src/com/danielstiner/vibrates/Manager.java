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

public class Manager {
	
	private static final String PREFIX = "com.danielstiner.vibrates.manager";
	
	public static final String ENTITY_ID_KEY = PREFIX + ".id";
	
	private CCMHelper _dbHelper;
	
	private Context _context;
	
	private static final int version = 2;
	
	private static final String DEBUG_TAG = "VibratesManager";
	
	protected static final String KEY_ENTITY_ROWID = "_id";
	protected static final String KEY_ENTITY_ID = "id";
	protected static final String KEY_ENTITY_KIND = "kind";
	protected static final String KEY_ENTITY_NAME = "name";
	protected static final String KEY_ENTITY_PATTERN = "pattern";
	protected static final String KEY_ENTITY_TIMES_CONTACTED = "times";
	protected static final String KEY_LOOKUP_ROWID = "_id";
	protected static final String KEY_LOOKUP_KIND = "kind";
	protected static final String KEY_LOOKUP_ENTITYID = "entity";
	protected static final String KEY_LOOKUP_IDENTIFIER = "identifier";

	protected static final String TABLE_ENTITIES = "entities";
	protected static final String TABLE_LOOKUP = "lookuptable";

    public Manager(Context c) {
        _dbHelper = new CCMHelper(c, "vibrates", null, version);
        
        // TODO check c for nulls
        _context = c;
    }
    protected SQLiteDatabase getWritableDatabase()
    {
    	return _dbHelper.getWritableDatabase();
    }
    protected SQLiteDatabase getReadableDatabase()
    {
    	return _dbHelper.getReadableDatabase();
    }
    
	private class CCMHelper extends SQLiteOpenHelper {

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
    
}
