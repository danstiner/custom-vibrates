/**
 * 
 */
package com.danielstiner.vibrates;

import com.danielstiner.vibrates.database.EntityManager;

public class Entity {
	
	private static final String CLASSNAME = com.danielstiner.vibrates.Vibrates.NS + "." + "Entity";
	
	public static final String ID_BUNDLE_KEY = CLASSNAME + "." + "id";

	public static final String TYPE_CONTACTSCONTRACTCONTACT = "contacts_contract_contact";

	
	private Long _id;
	
	private String _identifier;
	
	/**
	 * Create default entity representing nobody in particular
	 * @param identifier
	 */
	public Entity() {
		this(null, new Long(EntityManager.ID_NOBODY));
	}
	
	/**
	 * Create entity from globally unique identifier string
	 * @param identifier
	 */
	public Entity(String identifier) {
		this(identifier, null);
	}
	/**
	 * Create entity from id unique for every entity in this app
	 * @param id
	 */
	public Entity(Long id) {
		this(null, id);
	}
	/**
	 * Better, two ways to lookup the entity
	 * @param identifier
	 * @param id
	 */
	public Entity(String identifier, Long id) {
		_identifier = identifier;
		_id = id;
	}
	public String identifier() {
		return _identifier;
	}
	public Long entityid() {
		return _id;
	}
	public Entity entityid(Long entityid) {
		_id = entityid;
		return this;
	}
}