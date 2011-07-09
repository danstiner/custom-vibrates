/**
 * 
 */
package com.danielstiner.vibrates;

public class Entity {
	

	public static final String TYPE_CONTACTSCONTRACTCONTACT = null;

	private Long _id;
	
	private String _identifier;
	
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
	public String getIdentifier() {
		return _identifier;
	}
	public Long getId() {
		return _id;
	}
	private void updateId(Long update_id) {
		_id = update_id;
	}
}