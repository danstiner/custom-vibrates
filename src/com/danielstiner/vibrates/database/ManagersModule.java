package com.danielstiner.vibrates.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class ManagersModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IEntityManager.class).to(EntityManager.class);
		bind(IIdentifierManager.class).to(IdentifierManager.class);
		bind(IPatternManager.class).to(PatternManager.class);
	}

}
