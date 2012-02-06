package com.danielstiner.vibrates.storage.internal;

import com.danielstiner.vibrates.storage.IEntityFilter;
import com.danielstiner.vibrates.storage.IManager;
import com.danielstiner.vibrates.storage.internal.IDatabase.IHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class StorageModule extends AbstractModule {

	private static final String DATABASE_NAME = "vibrates";

	protected static final String DATABASE_NAME_KEY = "database_name";

	@Override
	protected void configure() {
		bind(IManager.class).to(Manager.class);
		bind(IEntityFilter.class).to(EntityFilter.class);
		bind(IDatabase.class).to(Database.class);
		bind(IEntityStore.class).to(EntityStore.class);
		bind(IIdentifierStore.class).to(IdentifierStore.class);
		bindConstant().annotatedWith(Names.named(DATABASE_NAME_KEY)).to(
				DATABASE_NAME);
	}

	@Provides
	IHelper[] provideDatabaseHelpers() {
		return new IHelper[] { new EntityStore.Helper(),
				new IdentifierStore.Helper() };
	}

}
