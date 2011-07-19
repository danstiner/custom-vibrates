package com.danielstiner.vibrates.database;

import com.danielstiner.vibrates.Entity;

public interface IPatternManager {

	long[] get(Entity entity, String notificationType);

}
