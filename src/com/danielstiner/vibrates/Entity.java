/**
 * 
 */
package com.danielstiner.vibrates;

import android.os.Bundle;

public class Entity {
	
	static final String CLASSNAME = com.danielstiner.vibrates.Vibrates.NS + "." + "Entity";
	
	public static final String EXTRA_KEY_ID = CLASSNAME + "." + "id";

	public static final String TYPE_CONTACTSCONTRACTCONTACT = "contacts_contract_contact";
	
	/**  */
	public static final int ID_NOBODY = -3;

	public static final int ID_DEFAULT = -4;

	private Long _id;

	private Bundle _extras;
	
	/**
	 * Create default entity representing nobody in particular
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
	 * Get a map of extra data about the entity, generally used by EntityManager to cache information
	 * 
	 * @return Bundle of extra information describing the entity
	 */
	public Bundle getExtras()
	{
		if(_extras == null)
			_extras = new Bundle();
		
		return _extras;
	}
}