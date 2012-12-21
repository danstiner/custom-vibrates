package com.danielstiner.vibrates.model.internal;

import java.io.InputStream;
import java.util.List;

import roboguice.inject.ContextScopedProvider;
import roboguice.inject.ContextSingleton;
import roboguice.util.Ln;
import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.LoaderManager;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Entity.Kind;
import com.danielstiner.vibrates.Identifier;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.model.IEntityFilter;
import com.danielstiner.vibrates.util.PatternUtil;
import com.google.inject.Inject;
import com.google.inject.Provider;

@ContextSingleton
public class DataModel implements IDataModel {

	@Inject
	private Application mContext;

	private IIdentifierStore mIdentifierStore;

	@Inject
	private ContextScopedProvider<IIdentifierStore> mIdentifierStoreProvider;

	private IEntityStore mEntityStore;

	@Inject
	private ContextScopedProvider<IEntityStore> mEntityStoreProvider;

	@Inject
	private Provider<Application> mContextProvider;

	@Inject
	private Provider<Identifier> mIdentifierProvider;

	@Override
	public Entity entityFromCursor(Cursor c) {
		return getEntityStore().fromCursor(c);
	}

	@Override
	public Drawable getPicture(Entity e) {
		// TODO Switch based on entity kind maybe?
		Cursor c = getIdentifierStore().search(e,
				IdentifierStore.KIND_CONTACTS_CONTRACT_ID);

		if (c == null || !c.moveToFirst())
			return null;

		String contacts_contract_idstring = getIdentifierStore().fromCursor(c)
				.getIdentifier();

		try {
			Long contacts_contract_id = Long
					.parseLong(contacts_contract_idstring);

			Uri uri = ContentUris
					.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
							contacts_contract_id);
			InputStream input = Contacts.openContactPhotoInputStream(
					mContext.getContentResolver(), uri);
			return Drawable.createFromStream(input, "contactphoto");
		} catch (NumberFormatException except) {
			return null;
		}
	}

	private IIdentifierStore getIdentifierStore() {

		if (mIdentifierStore == null)
			mIdentifierStore = mIdentifierStoreProvider.get(mContextProvider
					.get().getApplicationContext());
		return mIdentifierStore;
	}

	@Override
	public void searchEntities(LoaderManager loaderManager,
			IEntityFilter filter, ISearchCallback callback) {
		getEntityStore().search(loaderManager, filter, callback);
	}

	@Override
	public Entity createFromContactUri(Uri contact_uri) {
		if (contact_uri == null)
			return null;
		// Example uri:
		// content://com.android.contacts/contacts/lookup/0r7-2C46324E483C324A3A484634/7
		Ln.v("Got a result: %s", contact_uri.toString());
		// get the contact id from the Uri
		Long origId = Long.parseLong(contact_uri.getLastPathSegment());

		// get the permanent lookup key in case the id changes
		List<String> resultSegments = contact_uri.getPathSegments();
		// hint, its the second to last segment
		String origLookupKey = resultSegments.get(resultSegments.size() - 2);

		// TODO fix identifier assumptions
		// Grab some info from the system contact service
		Cursor c = mContext.getContentResolver().query(
				ContactsContract.Contacts.getLookupUri(origId, origLookupKey),
				new String[] { ContactsContract.Contacts.DISPLAY_NAME,
						ContactsContract.Contacts.LOOKUP_KEY,
						ContactsContract.Contacts._ID }, null, null, null);
		String name;
		String lookup;
		Long id;
		try {
			c.moveToFirst();
			// Grab some info
			name = c.getString(c
					.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			lookup = c
					.getString(c
							.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
			id = c.getLong(c
					.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
			// TODO: Catch clause in case something goes wrong
		} finally {
			c.close();
		}
		if (name == null || lookup == null)
			return null;

		// Generate a default contact pattern
		Pattern pattern = Pattern.fromArray(PatternUtil.generate(name));

		Entity e = create(lookup, name, pattern, Entity.Kind.Contact);

		// Add some lookup identifiers
		getIdentifierStore().add(
				e,
				mIdentifierProvider.get().setIdentifier(name)
						.setKind(Identifier.Kind.SystemContactName));
		getIdentifierStore().add(
				e,
				mIdentifierProvider.get().setIdentifier(lookup)
						.setKind(Identifier.Kind.SystemContactLookup));
		getIdentifierStore().add(
				e,
				mIdentifierProvider.get().setIdentifier(id.toString())
						.setKind(Identifier.Kind.SystemContactId));

		return e;

	}

	@Override
	public Uri getContactUri(Entity e) {
		// TODO Auto-generated method stub
		// return ContactsContract.Contacts.getLookupUri(c.entityid(),
		// c.identifier());
		return null;
	}

	@Override
	public Entity getEntity(String identifier, Kind kindGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pattern getPattern(Entity e) {
		if (e == null)
			return (Pattern) null;
		else
			return e.getPattern();
	}

	@Override
	public void remove(Entity entity) {
		getEntityStore().delete(entity);

		// TODO remove identifiers
	}

	private Entity create(String identifier, String name, Pattern pattern,
			Kind kind) {
		// First first see if such an entity already exists
		Entity entity = getEntity(identifier, kind);
		if (entity != null)
			return entity;

		// Do some validation
		if (!PatternUtil.isValid(pattern)) {
			Ln.e("Invalid pattern given to create a new contact.");
		}

		// Try and add such an entity
		entity = getEntityStore().create(name, pattern, kind);

		// Now map some identifiers
		// getIdentifierManager().add(entity, identifier);
		// getIdentifierManager().add(entity, name);

		return entity;
	}

	@Override
	public Entity getEntity(long id) {
		return getEntityStore().get(id);
	}

	@Override
	public void update(Entity entity) {
		getEntityStore().update(entity);
	}

	private IEntityStore getEntityStore() {

		if (mEntityStore == null)
			mEntityStore = mEntityStoreProvider.get(mContextProvider.get()
					.getApplicationContext());

		return mEntityStore;
	}
}

//
// @Override
// public Uri getContactUri(Entity entity) {
// Cursor c_id = getIdentifierManager().search(entity,
// IdentifierStore.KIND_CONTACTS_CONTRACT_ID);
// Cursor c_lookup = getIdentifierManager().search(entity,
// IdentifierStore.KIND_CONTACTS_CONTRACT_LOOKUP);
//
// if (!c_lookup.moveToFirst() || !c_id.moveToFirst())
// return null;
//
// String contacts_contract_idstring = getIdentifierManager()
// .identifierFromCursor(c_id);
// String contacts_contract_lookup = getIdentifierManager()
// .identifierFromCursor(c_lookup);
//
// try {
// Long contacts_contract_id = Long
// .parseLong(contacts_contract_idstring);
//
// // Build uri
// return Contacts.getLookupUri(contacts_contract_id,
// contacts_contract_lookup);
//
// } catch (NumberFormatException e) {
// return null;
// }
// }

// package com.danielstiner.vibrates.model.internal;
//
// import java.io.InputStream;
// import java.util.List;
//
// import roboguice.inject.ContextSingleton;
// import roboguice.util.Ln;
// import android.content.ContentUris;
// import android.content.Context;
// import android.content.Intent;
// import android.database.Cursor;
// import android.net.Uri;
// import android.provider.ContactsContract;
// import android.provider.ContactsContract.Contacts;
// import android.support.v4.app.LoaderManager;
// import android.support.v4.app.LoaderManager.LoaderCallbacks;
// import android.support.v4.content.Loader;
//
// import com.danielstiner.vibrates.Entity;
// import com.danielstiner.vibrates.Pattern;
// import com.danielstiner.vibrates.model.IEntityFilter;
// import com.danielstiner.vibrates.model.IDataModel;
// import com.danielstiner.vibrates.util.PatternUtil;
// import com.google.inject.Inject;
// import com.google.inject.Provider;
//
// @ContextSingleton
// public class Manager implements IDataModel {
//
// private static final long[] DEFAULT_PATTERN = { 0, 500, 500, 500 };
//
// private Context context;
// private Provider<IEntityStore> entitymanager_provider;
// @Inject
// private Provider<IIdentifierStore> identifiermanager_provider;
//
// @Inject
// private Provider<Pattern> pattern_provider;
//
// @Inject
// private IEntityStore entity_manager;
// @Inject
// private IIdentifierStore identifier_manager;
//
// @Inject
// private Context mContext;
//
// @Inject
// public Manager(
// // Provider<Entity> entity_provider,
// Context activity
// // Provider<IEntityManager> entitymanager_provider,
// // Provider<IIdentifierManager> identifiermanager_provider,
// // Provider<IPatternManager> patternmanager_provider) {
// ) {
// // this.entity_provider = entity_provider;
// this.context = activity;
// // this.entitymanager_provider = entitymanager_provider;
// // this.identifiermanager_provider = identifiermanager_provider;
// // this.patternmanager_provider = patternmanager_provider;
// }
//
// public Entity createFromGroupId(String groupid) {
// if (groupid == null)
// return null;
// // Example uri:
// //
// content://com.android.contacts/contacts/lookup/0r7-2C46324E483C324A3A484634/7
// Ln.v("Create group from: %s", groupid);
//
// // TODO fix identifier assumptions
// // Grab some info from the system contacts service
// Cursor c = context.getContentResolver().query(
// ContactsContract.Groups.CONTENT_URI,
// new String[] { ContactsContract.Groups.TITLE,
// ContactsContract.Groups._ID },
// ContactsContract.Groups._ID + " = ?", new String[] { groupid },
// null);
// String title;
// Long id;
// try {
// c.moveToFirst();
// // Grab some info
// title = c.getString(c
// .getColumnIndexOrThrow(ContactsContract.Groups.TITLE));
// ;
// id = c.getLong(c.getColumnIndexOrThrow(ContactsContract.Groups._ID));
// // TODO: Catch clause in case something goes wrong
// } finally {
// c.close();
// }
// if (id == null || title == null)
// return null;
//
// // Generate a default contact pattern
// long[] pattern = PatternUtil.generate(title);
//
// Entity e = create(id.toString(), title, pattern, Entity.KIND_GROUP);
//
// // Add some lookup identifiers
// getIdentifierManager().add(e, title,
// IdentifierStore.KIND_CONTACTS_GROUP_TITLE);
// getIdentifierManager().add(e, id.toString(),
// IdentifierStore.KIND_CONTACTS_GROUP_ID);
//
// return e;
// }
//
// private Entity create(String identifier, String name, long[] pattern,
// Entity.Kind type) {
// // First first see if such an entity already exists
// Entity entity = getEntity(identifier, type);
// if (entity != null)
// return entity;
//
// // Do some validation
// if (!PatternUtil.isValid(pattern)) {
// Ln.e("Invalid pattern given to create a new contact.");
// }
//
// // Try and add such an entity
// entity = getEntityManager().create(name, pattern, type);
//
// // Now map some identifiers
// // getIdentifierManager().add(entity, identifier);
// // getIdentifierManager().add(entity, name);
//
// return entity;
// }
//
// public Entity getEntity(String identifier) {
// Cursor c = getIdentifierManager().get(identifier);
// if (c.moveToFirst()) {
// return getIdentifierManager().entityFromCursor(c);
// }
// return null;
// }
//
// // @Override
// // public Entity[] getEntities(String identifier) {
// // ArrayList<Entity> entities = new ArrayList<Entity>();
// // Cursor c = getIdentifierManager().get(identifier);
// // }
//
// public Entity getEntity(String identifier, Entity.Kind kind) {
// Cursor c = getIdentifierManager().get(identifier);
// if (c.moveToFirst()) {
// do {
// if (getKind(getIdentifierManager().entityFromCursor(c)).equals(
// kind)) {
// return getIdentifierManager().entityFromCursor(c);
// }
// } while (c.moveToNext());
// }
// return null;
// }
//
//
// @Override
// public Cursor getEntities() {
// // TODO return getEntityManager().getAll();
// return null;
// }
//
// @Override
// public String getDisplayName(Entity entity) {
// // TODO return getEntityManager().getDisplayName(entity);
// return null;
// }
//
// @Override
// public Entity.Kind getKind(Entity entity) {
// // TODO return getEntityManager().getKind(entity);
// return null;
// }
//
// // TODO, remove, should be able to use getContactUri to replace this
// @Override
// public InputStream getPicture(Entity entity) {
// // TODO Switch based on entity kind maybe?
// Cursor c = getIdentifierManager().search(entity,
// IdentifierStore.KIND_CONTACTS_CONTRACT_ID);
//
// if (c == null || !c.moveToFirst())
// return null;
//
// String contacts_contract_idstring = getIdentifierManager()
// .identifierFromCursor(c);
//
// try {
// Long contacts_contract_id = Long
// .parseLong(contacts_contract_idstring);
//
// Uri uri = ContentUris
// .withAppendedId(ContactsContract.Contacts.CONTENT_URI,
// contacts_contract_id);
// InputStream input = Contacts.openContactPhotoInputStream(
// context.getContentResolver(), uri);
// return input;
// } catch (NumberFormatException e) {
// return null;
// }
// }
//
// @Override
// public Uri getContactUri(Entity entity) {
// Cursor c_id = getIdentifierManager().search(entity,
// IdentifierStore.KIND_CONTACTS_CONTRACT_ID);
// Cursor c_lookup = getIdentifierManager().search(entity,
// IdentifierStore.KIND_CONTACTS_CONTRACT_LOOKUP);
//
// if (!c_lookup.moveToFirst() || !c_id.moveToFirst())
// return null;
//
// String contacts_contract_idstring = getIdentifierManager()
// .identifierFromCursor(c_id);
// String contacts_contract_lookup = getIdentifierManager()
// .identifierFromCursor(c_lookup);
//
// try {
// Long contacts_contract_id = Long
// .parseLong(contacts_contract_idstring);
//
// // Build uri
// return Contacts.getLookupUri(contacts_contract_id,
// contacts_contract_lookup);
//
// } catch (NumberFormatException e) {
// return null;
// }
// }
//
// @Override
// public long[] getPattern(Entity entity, String type) {
// // TODO
// // long[] entity_pattern = getEntityManager().getPattern(entity);
// //
// // // TODO type based pattern appending
// // if (entity_pattern != null)
// // return entity_pattern;
// // else
// // return DEFAULT_PATTERN;
// return null;
// }
//
// @Override
// public Pattern getPattern(Entity e) {
// // long[] entity_pattern = getEntityManager().getPattern(e);
// //
// // return pattern_provider.get().pattern(entity_pattern);
// // TODO
// return null;
// }
//
// @Override
// public void setPattern(Entity entity, long[] pattern) {
// // TODO getEntityManager().setPattern(entity, pattern);
// }
//
// private IEntityStore getEntityManager() {
// if (entity_manager == null)
// entity_manager = entitymanager_provider.get();
// return entity_manager;
// }
//
// private IIdentifierStore getIdentifierManager() {
// if (identifier_manager == null)
// identifier_manager = identifiermanager_provider.get();
// return identifier_manager;
// }
//
// @Override
// public void update(Entity entity) {
// // TODO Refresh.update(entity, getEntityManager(),
// // getIdentifierManager(),
// // context);
// }
//
// @Override
// public void remove(Entity entity) {
// getEntityManager().delete(entity);
// getIdentifierManager().removeAll(entity);
// }
//
// @Override
// public Intent getViewIntent(Entity entity) {
// if (getKind(entity).equals(Entity.KIND_CONTACT)) {
// Cursor ids = getIdentifierManager().search(entity,
// IdentifierStore.KIND_CONTACTS_CONTRACT_ID);
//
// if (ids.getCount() != 1)
// return null;
//
// ids.moveToFirst();
//
// Long id = Long.parseLong(getIdentifierManager()
// .identifierFromCursor(ids));
//
// return new Intent(Intent.ACTION_VIEW,
// Uri.parse("content://contacts/people/" + id.toString()));
// } else {
// return null;
// }
// }
// }
