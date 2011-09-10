package com.danielstiner.vibrates.utility;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IIdentifierManager;

public class Refresh {

	public static void update(Entity entity,
			IEntityManager entityManager,
			IIdentifierManager identifierManager) {
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
