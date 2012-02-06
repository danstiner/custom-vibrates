/**
 * 
 */
package com.danielstiner.vibrates;

import com.danielstiner.vibrates.storage.VibrateEntityProvider;

import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

public class Entity {
	
	public enum Kind {
		
		Contact("contacts_contract_contact"),
		Group("contacts_contract_group"),
		App("contact_vibrates_apptype");
		
		private String mName;
		private Kind(String name)
		{
			mName = name;
		}
		@Override
		public String toString()
		{
			return mName;
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

	private Long _id;

	private Bundle _extras;

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
		getExtras().putLong(EXTRA_KEY_ID, entityid);
		return this;
	}

	/**
	 * Get a map of extra data about the entity, generally used by EntityManager
	 * to cache information
	 * 
	 * @return Bundle of extra information describing the entity
	 */
	public Bundle getExtras() {
		if (_extras == null)
			_extras = new Bundle();

		return _extras;
	}
	
	public static final class Entities implements BaseColumns {
		
		public Entities() {
			
		}
		
		public static final Uri CONTENT_URI = VibrateEntityProvider.CONTENT_URI;
	
		public static final String CONTENT_TYPE = VibrateEntityProvider.CONTENT_ITEM_TYPE;
		
		public static final String ENTITY_ID = "_id";
		
		public static final String KIND = "kind";
		public static final String NAME = "name";
		public static final String PATTERN = "pattern";
		public static final String NOTIFY_COUNT = "notified";
	}
}