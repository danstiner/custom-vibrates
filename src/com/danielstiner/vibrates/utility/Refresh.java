package com.danielstiner.vibrates.utility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IIdentifierManager;
import com.danielstiner.vibrates.database.IdentifierManager;

public class Refresh {

	public static void update(Entity entity,
			IEntityManager entityManager,
			IIdentifierManager identifierManager,
			Context context) {
		// TODO others
		String contactid = getContactId(entity, identifierManager);
		
		if(contactid == null)
			return;
		
		updatePhoneNumbers(entity, contactid, context.getContentResolver(), identifierManager);
		updateEmails(entity, contactid, context.getContentResolver(), identifierManager);
	}
	
	private static String getContactId(Entity entity, IIdentifierManager identifierManager) {
		// See if we have a single contacts contract id to use for this entity
		Cursor contactIds = null;
		try {
			contactIds = identifierManager.get(entity, IdentifierManager.KIND_CONTACTS_CONTRACT_ID);
			
			if(contactIds.getCount() != 1 || !contactIds.moveToFirst())
				return null;
			
			return identifierManager.identifierFromCursor(contactIds);
		} finally {
			if(contactIds != null)
				contactIds.close();
		}
	}
	
	private static void updatePhoneNumbers(Entity entity, String contactid, ContentResolver cr, IIdentifierManager identifierManager) {
		
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
				contactid,
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
				},
			null
			);
		
		// Remove all old phone number identifiers
		identifierManager.removeAll(entity, NotificationTypes.VOICE_PHONECALL);
		identifierManager.removeAll(entity, NotificationTypes.CHAT_TEXT);
		
		if(c.getCount() < 1) return;
		
		// Add phone numbers back
		c.moveToFirst();
		while(!c.isAfterLast()) {
			// FIXME: Some fancy parsing of this number?
			String number = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
			
			// Strip any extra characters that are not numbers
			number = number.replaceAll("[^0-9]", "");
			
			// Drop the one (US country code) from the beginning if its there
			number = number.length() == 11 && number.charAt(0) == '1' ? number.substring(1) : number;
			
			identifierManager.add(entity, number, NotificationTypes.VOICE_PHONECALL);
			identifierManager.add(entity, number, NotificationTypes.CHAT_TEXT);
			
			c.moveToNext();
		}
		
		c.close();
	}
	
	private static void updateEmails(Entity entity, String contactid, ContentResolver cr, IIdentifierManager identifierManager) {
		
		Cursor c = cr.query(
			ContactsContract.CommonDataKinds.Email.CONTENT_URI,
			new String[] {
				// Should be ContactsContract.CommonDataKinds.Email.ADDRESS, dunno why it won't work
				// Data1 should map to the same field, so no matter
				ContactsContract.CommonDataKinds.Email.DATA1
				},
			ContactsContract.Data.CONTACT_ID + " = ?",
			new String[] {
				contactid,
				},
			null
			);
		
		// Remove all old phone number identifiers
		identifierManager.removeAll(entity, NotificationTypes.MESSAGE_EMAIL);
		
		if(c.getCount() < 1) return;
		
		// Add phone numbers back
		c.moveToFirst();
		while(!c.isAfterLast()) {
			// FIXME: Some fancy parsing of this number?
			String address = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA1));
			
			identifierManager.add(entity, address, NotificationTypes.MESSAGE_EMAIL);
			
			c.moveToNext();
		}
		
		c.close();
	}
}
