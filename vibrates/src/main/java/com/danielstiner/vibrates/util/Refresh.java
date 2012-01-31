package com.danielstiner.vibrates.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IIdentifierManager;
import com.danielstiner.vibrates.database.IdentifierManager;

public class Refresh {

	public static void update(Entity entity, IEntityManager entityManager,
			IIdentifierManager identifierManager, Context context) {

		String kind = entityManager.getKind(entity);

		if (kind.equals(Entity.TYPE_CONTACT)) {
			String contactid = getContactId(entity, identifierManager);

			if (contactid == null)
				return;

			updatePhoneNumbers(entity, contactid, context.getContentResolver(),
					identifierManager);
			updateEmails(entity, contactid, context.getContentResolver(),
					identifierManager);
		} else if (kind.equals(Entity.TYPE_GROUP)) {
			String groupid = getGroupId(entity, identifierManager);

			if (groupid == null)
				return;

			updateGroupMembers(entity, groupid, context.getContentResolver(),
					identifierManager);
		}
	}

	private static String getContactId(Entity entity,
			IIdentifierManager identifierManager) {
		// See if we have a single contacts contract id to use for this entity
		Cursor contactIds = null;
		try {
			contactIds = identifierManager.get(entity,
					IdentifierManager.KIND_CONTACTS_CONTRACT_ID);

			if (contactIds.getCount() != 1 || !contactIds.moveToFirst())
				return null;

			return identifierManager.identifierFromCursor(contactIds);
		} finally {
			if (contactIds != null)
				contactIds.close();
		}
	}

	private static String getGroupId(Entity entity,
			IIdentifierManager identifierManager) {
		// See if we have a single contacts contract id to use for this entity
		Cursor contactIds = null;
		try {
			contactIds = identifierManager.get(entity,
					IdentifierManager.KIND_CONTACTS_GROUP_ID);

			if (contactIds.getCount() != 1 || !contactIds.moveToFirst())
				return null;

			return identifierManager.identifierFromCursor(contactIds);
		} finally {
			if (contactIds != null)
				contactIds.close();
		}
	}

	private static void updatePhoneNumbers(Entity map_to_entity,
			String contactid, ContentResolver cr,
			IIdentifierManager identifierManager) {

		// Remove all old phone number identifiers
		identifierManager.removeAll(map_to_entity,
				NotificationTypes.VOICE_PHONECALL);
		identifierManager.removeAll(map_to_entity, NotificationTypes.CHAT_TEXT);

		addPhoneNumbers(map_to_entity, contactid, cr, identifierManager);
	}

	private static void addPhoneNumbers(Entity e, String contactid,
			ContentResolver cr, IIdentifierManager identifierManager) {

		Cursor c = cr
				.query(ContactsContract.Data.CONTENT_URI,
						new String[] { ContactsContract.Data._ID,
								ContactsContract.CommonDataKinds.Phone.NUMBER,
								ContactsContract.CommonDataKinds.Phone.TYPE,
								ContactsContract.CommonDataKinds.Phone.LABEL },
						ContactsContract.Data.CONTACT_ID + " = ?" + " AND "
								+ ContactsContract.Data.MIMETYPE + " = ?",
						new String[] {
								contactid,
								ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE },
						null);

		if (c.getCount() < 1)
			return;

		// Add phone numbers back
		c.moveToFirst();
		while (!c.isAfterLast()) {
			String number = c
					.getString(c
							.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

			// // Strip any extra characters that are not numbers
			// number = number.replaceAll("[^0-9]", "");
			//
			// // Drop the one (US country code) from the beginning if its there
			// number = number.length() == 11 && number.charAt(0) == '1' ?
			// number.substring(1) : number;

			// Format all numbers as international versions
			String international_number = IdentifierUtil
					.phoneNumberToInternational(number);

			identifierManager.add(e, international_number,
					NotificationTypes.VOICE_PHONECALL);
			identifierManager.add(e, international_number,
					NotificationTypes.CHAT_TEXT);

			c.moveToNext();
		}

		c.close();
	}

	// private static void addDisplayNames(Entity map_to_entity, String
	// contactid,
	// ContentResolver cr, IIdentifierManager identifierManager) {
	//
	// Cursor c = cr
	// .query(ContactsContract.Data.CONTENT_URI,
	// new String[] { ContactsContract.Data._ID,
	// ContactsContract.CommonDataKinds.Phone.NUMBER,
	// ContactsContract.CommonDataKinds.Phone.TYPE,
	// ContactsContract.CommonDataKinds.Phone.LABEL },
	// ContactsContract.Data.CONTACT_ID + " = ?" + " AND "
	// + ContactsContract.Data.MIMETYPE + " = ?",
	// new String[] {
	// contactid,
	// ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE },
	// null);
	//
	// if (c.getCount() < 1)
	// return;
	//
	// // Add phone numbers back
	// c.moveToFirst();
	// while (!c.isAfterLast()) {
	// // FIXME: Some fancy parsing of this number?
	// String number = c
	// .getString(c
	// .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
	//
	// // Strip any extra characters that are not numbers
	// number = number.replaceAll("[^0-9]", "");
	//
	// // Drop the one (US country code) from the beginning if its there
	// number = number.length() == 11 && number.charAt(0) == '1' ? number
	// .substring(1) : number;
	//
	// identifierManager.add(map_to_entity, number,
	// NotificationTypes.VOICE_PHONECALL);
	// identifierManager.add(map_to_entity, number,
	// NotificationTypes.CHAT_TEXT);
	//
	// c.moveToNext();
	// }
	//
	// c.close();
	// }

	private static void updateEmails(Entity entity, String contactid,
			ContentResolver cr, IIdentifierManager identifierManager) {

		Cursor c = cr.query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI,
				new String[] {
				// Should be ContactsContract.CommonDataKinds.Email.ADDRESS,
				// dunno why it won't work
				// Data1 should map to the same field, so no matter
				ContactsContract.CommonDataKinds.Email.DATA1 },
				ContactsContract.Data.CONTACT_ID + " = ?",
				new String[] { contactid, }, null);

		// Remove all old phone number identifiers
		identifierManager.removeAll(entity, NotificationTypes.MESSAGE_EMAIL);

		if (c.getCount() < 1)
			return;

		// Add phone numbers back
		c.moveToFirst();
		while (!c.isAfterLast()) {
			// FIXME: Some fancy parsing of this?
			String address = c
					.getString(c
							.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA1));

			identifierManager.add(entity, address,
					NotificationTypes.MESSAGE_EMAIL);

			c.moveToNext();
		}

		c.close();
	}

	private static void updateGroupMembers(Entity group, String groupid,
			ContentResolver contentResolver,
			IIdentifierManager identifierManager) {

		// TODO fix identifier assumptions
		// Grab some info from the system contacts service
		Cursor info = contentResolver.query(
				ContactsContract.Groups.CONTENT_URI, new String[] {
						ContactsContract.Groups.TITLE,
						ContactsContract.Groups._ID },
				ContactsContract.Groups._ID + " = ?", new String[] { groupid },
				null);
		String title;
		Long id;
		try {
			info.moveToFirst();
			// Grab some info
			title = info.getString(info
					.getColumnIndexOrThrow(ContactsContract.Groups.TITLE));
			;
			id = info.getLong(info
					.getColumnIndexOrThrow(ContactsContract.Groups._ID));
			// TODO: Catch clause in case something goes wrong
		} finally {
			info.close();
		}
		if (id == null || title == null)
			return;

		// Remove all old identifiers
		identifierManager.removeAll(group);

		// Add some lookup identifiers
		identifierManager.add(group, title,
				IdentifierManager.KIND_CONTACTS_GROUP_TITLE);
		identifierManager.add(group, id.toString(),
				IdentifierManager.KIND_CONTACTS_GROUP_ID);

		// remove
		Cursor members = contentResolver
				.query(ContactsContract.Data.CONTENT_URI,
						new String[] {
								ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID,
								ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME },
						ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID
								+ " = ? OR "
								+ ContactsContract.CommonDataKinds.GroupMembership.GROUP_SOURCE_ID
								+ " = ?", new String[] { groupid, groupid },
						null);

		if (members.getCount() < 1)
			return;

		// Add identifiers back
		members.moveToFirst();
		while (!members.isAfterLast()) {
			Long contactid = members
					.getLong(members
							.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));

			// Add the identifiers for this contact
			addPhoneNumbers(group, contactid.toString(), contentResolver,
					identifierManager);

			// From our current query
			identifierManager
					.add(group,
							members.getString(members
									.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME)),
							IdentifierManager.KIND_CONTACTS_CONTRACT_NAME);

			members.moveToNext();
		}

		members.close();
	}
}
