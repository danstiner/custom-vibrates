package com.danielstiner.vibrates.model.internal;

import android.content.Context;
import android.database.Cursor;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Identifier;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class IdentifierStore implements IIdentifierStore {

	@Inject
	private Provider<Identifier> mIdentifierProvider;

	@Inject
	private Provider<Context> mContextProvider;

	public static final String KIND_DEFAULT = "unknown";
	public static final Identifier KIND_CONTACTS_CONTRACT_ID = new Identifier(
			null, Identifier.Kind.SystemContactId);
	public static final String KIND_CONTACTS_CONTRACT_NAME = "generated.contacts_contract_name";
	public static final String KIND_CONTACTS_CONTRACT_LOOKUP = "generated.contacts_contract_lookup";

	public static final String KIND_CONTACTS_GROUP_ID = "generated.contacts_group_id";
	public static final String KIND_CONTACTS_GROUP_TITLE = "generated.contacts_group_title";

	// protected static final String DEFAULT_KIND = "";

	// @Override
	// public Cursor getOrphans() {
	// return getByEntityId(new Long(Entity.ID_NOBODY));
	// }
	//
	// @Override
	// public Cursor get(Entity owner) {
	// if (owner == null)
	// return null;
	// else
	// return getByEntityId(owner.entityid());
	// }

	// private Cursor getByEntityId(Long entityid) {
	// // Open a connection to the database
	// SQLiteDatabase sql_db = db.getReadableDatabase();
	// try {
	// // Grab all contacts
	// return sql_db.query(TABLE, null, KEY_ENTITYID + " = ?",
	// new String[] { entityid.toString() }, null, null, null);
	// } finally {
	// if (sql_db != null)
	// sql_db.close();
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.danielstiner.vibrates.IIdentifierManager#get(java.lang.String)
	 */
	// public Cursor get(String identifier) {
	// if (identifier == null)
	// return null;
	//
	// return getByEntityIdentifier(identifier);

	// Cursor matches = getByEntityIdentifier(identifier);
	// // Make sure we only have one entity match
	// if(matches.getCount() > 1)
	// {
	// Ln.d("Found multiple (%d) entities for the identifier: %s",
	// matches.getCount(), identifier);
	// //return null;
	// }
	// if(!matches.moveToFirst())
	// return null;
	// Long entityid =
	// matches.getLong(matches.getColumnIndexOrThrow(KEY_ENTITYID));
	// // Must have worked
	// return entity_provider.get().entityid(entityid);
	// }

	// public Cursor get(String identifier, String kind) {
	// if (identifier == null || kind == null)
	// return null;
	//
	// return getByEntityIdentifierAndKind(identifier, kind);
	//
	// // Cursor matches = getByEntityIdentifier(identifier);
	// // // Make sure we only have one entity match
	// // if(matches.getCount() > 1)
	// // {
	// // Ln.d("Found multiple (%d) entities for the identifier: %s",
	// // matches.getCount(), identifier);
	// // //return null;
	// // }
	// // if(!matches.moveToFirst())
	// // return null;
	// // Long entityid =
	// // matches.getLong(matches.getColumnIndexOrThrow(KEY_ENTITYID));
	// // // Must have worked
	// // return entity_provider.get().entityid(entityid);
	// }
	//
	// private Cursor getByEntityIdentifier(String identifier) {
	// // Open a connection to the database
	// SQLiteDatabase sql_db = db.getReadableDatabase();
	// try {
	// // Grab all contacts for identifier
	// Cursor c = sql_db.query(TABLE, null, KEY_IDENTIFIER + " = ?",
	// new String[] { identifier }, null, null, null, null);
	//
	// if (c.isClosed())
	// Ln.d("Opening Cursor failed");
	//
	// return c;
	// } finally {
	// if (sql_db != null)
	// sql_db.close();
	// }
	// }
	//
	// private Cursor getByEntityIdentifierAndKind(String identifier, String
	// kind) {
	// // Open a connection to the database
	// SQLiteDatabase sql_db = db.getReadableDatabase();
	// try {
	// // Grab all contacts
	// Cursor c = sql_db.query(TABLE, null, KEY_IDENTIFIER + " = ?"
	// + " AND " + KEY_KIND + " = ? ", new String[] { identifier,
	// kind }, null, null, null, null);
	//
	// if (c.isClosed())
	// Ln.d("Opening Cursor failed");
	//
	// return c;
	// } finally {
	// if (sql_db != null)
	// sql_db.close();
	// }
	// }

	// @Override
	// public Cursor get(Entity owner, String kind) {
	// // Open a connection to the database
	// SQLiteDatabase sql_db = db.getReadableDatabase();
	// try {
	// // Grab all matching lookups
	// Cursor c = sql_db
	// .query(TABLE, null, KEY_ENTITYID + " = ? AND " + KEY_KIND
	// + " = ?", new String[] {
	// owner.entityid().toString(), kind }, null, null,
	// null, null);
	//
	// if (c.isClosed())
	// Ln.d("Opening Cursor failed");
	//
	// return c;
	//
	// } finally {
	// if (sql_db != null)
	// sql_db.close();
	// }
	// }
	//
	// @Override
	// public void add(Entity owner, String identifier, String kind) {
	// if (identifier == null) {
	// Ln.e("Will not add a null identifier");
	// return;
	// }
	// Long owner_id = (owner == null) ? Entity.ID_NOBODY : owner.entityid();
	// // Open a connection to the database
	// SQLiteDatabase sql_db = db.getWritableDatabase();
	// try {
	// ContentValues ident_values = new ContentValues(3);
	// ident_values.put(KEY_ENTITYID, owner_id.toString());
	// ident_values.put(KEY_IDENTIFIER, identifier);
	// ident_values.put(KEY_KIND, kind);
	// // TODO: throw is temporary
	// // long identifierId =
	// sql_db.insertOrThrow(TABLE, null, ident_values);
	//
	// } finally {
	// if (sql_db != null)
	// sql_db.close();
	// }
	// }
	//
	// @Override
	// public Entity entityFromCursor(Cursor c) {
	// Long entityid = c.getLong(c.getColumnIndexOrThrow(KEY_ENTITYID));
	// // Must have worked
	// return entity_provider.get().entityid(entityid);
	// }
	//
	// @Override
	// public String identifierFromCursor(Cursor c) {
	// return c.getString(c.getColumnIndexOrThrow(KEY_IDENTIFIER));
	// }
	//
	// @Override
	// public String kindFromCursor(Cursor c) {
	// return c.getString(c.getColumnIndexOrThrow(KEY_KIND));
	// }

	// @Override
	// public Entity entityFromCursor(Cursor c) {
	// return new Entity(
	// c.getString(c.getColumnIndexOrThrow(KEY_IDENTIFIER)),
	// c.getLong(c.getColumnIndexOrThrow(KEY_ENTITYID))
	// );
	// }

	// @Override
	// public int removeAll(Entity entity) {
	// if (entity == null || entity.entityid() == null) {
	// Ln.e("Cannot remove identifiers for a null entity.");
	// return 0;
	// }
	// // Open a connection to the database
	// SQLiteDatabase sql_db = db.getWritableDatabase();
	// try {
	// // Try and remove any identifiers for this entity
	// int delete_count = sql_db.delete(TABLE, KEY_ENTITYID + " = ?",
	// new String[] { entity.entityid().toString() });
	// return delete_count;
	// } finally {
	// if (sql_db != null)
	// sql_db.close();
	// }
	// }
	//
	// @Override
	// public int removeAll(Entity entity, String kind) {
	// if (entity == null || entity.entityid() == null) {
	// Ln.e("Cannot remove identifiers for a null entity.");
	// return 0;
	// }
	// // Open a connection to the database
	// SQLiteDatabase sql_db = db.getWritableDatabase();
	// try {
	// // Try and remove any identifiers for this entity
	// int delete_count = sql_db.delete(TABLE, KEY_ENTITYID + " = ? AND "
	// + KEY_KIND + " = ? ", new String[] {
	// entity.entityid().toString(), kind });
	// return delete_count;
	// } finally {
	// if (sql_db != null)
	// sql_db.close();
	// }
	// }

	public Cursor getOrphans() {
		// TODO Auto-generated method stub
		return null;
	}

	public Cursor get(Entity owner) {
		// TODO Auto-generated method stub
		return null;
	}

	public Cursor get(Entity owner, String kind) {
		// TODO Auto-generated method stub
		return null;
	}

	public void add(Entity to, String identifier, String kind) {
		// TODO Auto-generated method stub

	}

	public String identifierFromCursor(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	public String kindFromCursor(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	public int removeAll(Entity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int removeAll(Entity entity, String defaultPhonenumber) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Entity entityFromCursor(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor search(Entity owner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor search(Entity owner, Identifier kind) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor search(Identifier identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(Entity to, Identifier identifier) {
		// TODO Auto-generated method stub
		

	}

	@Override
	public Identifier fromCursor(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}
}
