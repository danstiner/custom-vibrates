package com.danielstiner.vibrates.database;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.utility.MorseCodePattern;
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

public class PatternManager implements IPatternManager {
	
	private static final String PREFIX = "com.danielstiner.vibrates.manager";
	
	public static final String ENTITY_ID_KEY = PREFIX + ".id";
	
	private static final String DEBUG_TAG = "VibratesEntityManager";
	
	/** Identifies patterns/lookups not attached to a contact (yet parent-less) */
	public static final int ID_NOBODY = -3;

	private IDatabase db;

	private Provider<Entity> entity_provider;
	
	@Inject
    public PatternManager(IDatabase db, Provider<Entity> entity_provider) {
    	this.db = db;
    	this.entity_provider = entity_provider;
    }
    
    public static long[] generate(String text) {
		// TODO Auto-generated method stub
		return MorseCodePattern.morsify(text);
	}

	public void clear(Entity entity) {
//		// First open a connection to the database
//    	SQLiteDatabase db = getWritableDatabase();
//    	// Attempt the deletion of all relevant records
//    	try {
//    		
//    		// TODO Don't forget about assocatied lookups
//    		
//            // Perform removal
//            int deleteCount = db.delete(
//            		TABLE_ENTITIES,
//            		KEY_ENTITY_ID + " == ?",
//            		new String[] {entity.entityid().toString()}
//            		);
//            
//            // Check if we actually removed a row
//            if( deleteCount == 0 ) {
//            	//Log.d(DEBUG_TAG, "Delete contact failed.", db.de);
//            }
//            
//    	} catch(Exception tr) {
//    		Log.d(DEBUG_TAG, "Delete contact failed.", tr);
//        } finally {
//            if (db != null)
//                db.close();
//            
//        }
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
