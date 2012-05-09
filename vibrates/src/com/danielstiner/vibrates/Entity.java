/**
 * 
 */
package com.danielstiner.vibrates;

import com.danielstiner.vibrates.model.Entities;
import com.danielstiner.vibrates.model.IDataModel;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

public class Entity {

	public enum Kind {

		Contact("contacts_contract_contact"), Group("contacts_contract_group"), App(
				"contact_vibrates_apptype");

		private String mName;

		private Kind(String name) {
			mName = name;
		}

		@Override
		public String toString() {
			return mName;
		}
		
		public String toDisplayString() {
			// TODO Localization
			return this.name();
		}

		public static Kind fromString(String name) {

			for (Kind k : Entity.Kind.values()) {
				if (k.mName.equals(name))
					return k;
			}

			throw new IllegalArgumentException(name + " not a valid kind name");

		}

	}

	static final String CLASSNAME = com.danielstiner.vibrates.Vibrates.NS + "."
			+ "Entity";

	public static final String EXTRA_KEY_ID = CLASSNAME + "." + "id";

	public static final Kind KIND_CONTACT = Kind.Contact;

	public static final Kind KIND_GROUP = Kind.Group;

	public static final Kind KIND_APP = Kind.App;

	/**  */
	public static final int ID_NOBODY = -3;

	public static final int ID_DEFAULT = -4;

	public final boolean put(Bundle bundle) {
		if(bundle == null)
			return false;
		bundle.putLong(EXTRA_KEY_ID, this.entityid());
		return true;
	}

	public static final Entity fromBundle(Bundle bundle, IDataModel dataModel) {
		if (bundle == null || !bundle.containsKey(EXTRA_KEY_ID))
			return null;

		long id = bundle.getLong(EXTRA_KEY_ID);

		return dataModel.getEntity(id);
	}

	public static final void toBundle(Bundle extras, Entity e) {
		if (e == null)
			return;

		e.put(extras);
	}
	
	public static void toIntent(Intent i, Entity e) {
		if(e != null && i != null)
			i.putExtra(EXTRA_KEY_ID, e.entityid());
	}

	private Long _id;

	private Kind mKind;

	private int mNotify;

	private String mName;

	private Pattern mPattern;

	/**
	 * Create default entity representing nobody in particular
	 * 
	 * @param identifier
	 */
	public Entity() {
		entityid(new Long(ID_NOBODY));
	}

	public Long entityid() {
		return _id;
	}

	public Entity entityid(Long entityid) {
		_id = entityid;
		return this;
	}

	public void setKind(Kind kind) {
		this.mKind = kind;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public void setNotifyCount(int count) {
		this.mNotify = count;
	}

	public void setPattern(Pattern pattern) {
		this.mPattern = pattern;
	}

	public String getName() {
		return mName;
	}

	public Pattern getPattern() {
		return mPattern;
	}

	public Kind getKind() {
		return mKind;
	}

	public ContentValues asContentValues() {

		ContentValues values = new ContentValues(3);
		values.put(Entities.NAME, getName());
		values.put(Entities.KIND, getKind().toString());
		values.put(Entities.PATTERN, getPattern().toString());

		return values;
	}

}