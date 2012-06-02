package com.danielstiner.vibrates.database;

import com.google.inject.AbstractModule;

public class ManagersModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IManager.class).to(Manager.class);

		bind(IEntityManager.class).to(EntityManager.class);
		bind(IIdentifierManager.class).to(IdentifierManager.class);
	}

}
