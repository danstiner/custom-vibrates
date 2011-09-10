package com.danielstiner.vibrates.utility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IIdentifierManager;

public class Refresh {

	public static void update(Entity entity,
			IEntityManager entityManager,
			IIdentifierManager identifierManager,
			Context context) {
		// TODO others
		updatePhoneNumbers(entity, context.getContentResolver(), identifierManager);
	}
	
	private static void updatePhoneNumbers(Entity entity, ContentResolver cr, IIdentifierManager identifierManager) {
		Cursor c = cr.query(
			ContactsContract.Data.CONTENT_URI,
			new String[] {
				ContactsContract.Data._ID,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.TYPE,
				ContactsContract.CommonDataKinds.Phone.LABEL
				},
			ContactsContract.Data.CONTACT_ID + " = ?" + " AND "
			+ ContactsContract.Data.MIMETYPE + " = ?",
			new String[] {
				entity.entityid().toString(),
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
				},
			null
			);
	
	
		// Remove all old phone number identifiers
		identifierManager.removeAll(entity, NotificationTypes.DEFAULT_PHONENUMBER);
		
		if(c.getCount() < 1) return;
		
		// Add phone numbers back
		c.moveToFirst();
		while(!c.isAfterLast()) {
			// FIXME: Some fancy parsing of this number?
			String number = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
			
			identifierManager.add(entity, number, NotificationTypes.DEFAULT_PHONENUMBER);
			
			c.moveToNext();
		}
	}
}
