package com.danielstiner.vibrates.storage.internal;

import com.danielstiner.vibrates.storage.internal.IDatabase.IDatabaseHelper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class StorageModule extends AbstractModule {

	private static final String DATABASE_NAME = "vibrates";

	protected static final String DATABASE_NAME_KEY = "database_name";

	@Override
	protected void configure() {
		bind(IDatabase.class).to(Database.class);
		bindConstant().annotatedWith(Names.named(DATABASE_NAME_KEY)).to(
				DATABASE_NAME);
	}

	@Provides
	IDatabaseHelper[] provideDatabaseHelpers() {
		return new IDatabaseHelper[] { new EntityStore.Helper(),
				new IdentifierStore.Helper() };
	}

}
