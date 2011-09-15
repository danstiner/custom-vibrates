package com.danielstiner.vibrates.database;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.utility.MorseCodePattern;

// TODO: Factor out this class entirely into a utility class
public class PatternManager implements IPatternManager {
	
	private static final String PREFIX = "com.danielstiner.vibrates.manager";
	
	public static final String ENTITY_ID_KEY = PREFIX + ".id";
	
	/** Identifies patterns/lookups not attached to a contact (yet parent-less) */
	public static final int ID_NOBODY = -3;
	
	// milliseconds per dit
	private static final long MORSE_MULTIPLIER = 89;
	
	// number of letters to use when morsifying a name or such
	private static final int MAX_MORSE = 3;

//	@Inject private IDatabase db;
//
//	private Provider<Entity> entity_provider;
//	
//	@Inject
//    public PatternManager(Provider<Entity> entity_provider) {
//    	this.entity_provider = entity_provider;
//    }
    
    public static long[] generate(String text) {
		// TODO Auto-generated method stub
    	
    	// pare down the text to the first few words
    	text = text.substring(0, Math.min(MAX_MORSE, text.length()));
    	
    	long[] morse = MorseCodePattern.morsify(text);
    	long[] ret = new long[morse.length + 1];
    	
    	ret[0] = 0;
    	for(int i=0; i<morse.length; i++) {
    		ret[i+1] = morse[i] * MORSE_MULTIPLIER;
    	}
    	
		return ret;
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
		return new long[] { 0, 300, 400, 500, 600 };
	}
	
	@Override
	public boolean isValid(long[] pattern) {
		if(pattern == null) return false;
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public long[] get(Entity entity) {
		return get(entity, null);
	}

	@Override
	public void set(Entity entity, long[] pattern) {
		// TODO Auto-generated method stub
		
	}
    
}
