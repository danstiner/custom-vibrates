package com.danielstiner.vibrates.database;

import com.danielstiner.vibrates.Entity;

public interface IEntityManager {

	public abstract String getDisplayName(Entity entity);

	public abstract Entity get(String identifier);

}
