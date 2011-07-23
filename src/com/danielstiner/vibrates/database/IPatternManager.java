package com.danielstiner.vibrates.database;

import com.danielstiner.vibrates.Entity;

public interface IPatternManager {

	public abstract long[] get(Entity entity);
	
	public abstract long[] get(Entity entity, String notificationType);
	
	public abstract void set(Entity entity, long[] pattern);

	/**
	 * 
	 * @param pattern
	 * @return
	 */
	public abstract boolean isValid(long[] pattern);
}
