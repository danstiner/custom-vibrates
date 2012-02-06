package com.danielstiner.vibrates.storage.internal;

import java.util.List;

import com.danielstiner.vibrates.Entity;

public interface IEntityStore {

	Entity get(Long id);
	
	List<Entity> getAll(Entity.Kind type);

	Entity create(String name, long[] pattern, Entity.Kind type);

	void delete(Entity entity);
	
	/** Commits changes made to entity
	 * 
	 * @param entity
	 */
	void update(Entity entity);

}
