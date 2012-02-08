package com.danielstiner.vibrates.model;

import com.danielstiner.vibrates.model.internal.Database;
import com.danielstiner.vibrates.model.internal.EntityFilter;
import com.danielstiner.vibrates.model.internal.EntityStore;
import com.danielstiner.vibrates.model.internal.IDatabase;
import com.danielstiner.vibrates.model.internal.IDatabase.IHelper;
import com.danielstiner.vibrates.model.internal.IEntityStore;
import com.danielstiner.vibrates.model.internal.IIdentifierStore;
import com.danielstiner.vibrates.model.internal.IdentifierStore;
import com.danielstiner.vibrates.model.internal.Manager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class StorageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IManager.class).to(Manager.class);
		bind(IEntityFilter.class).to(EntityFilter.class);
		bind(IDatabase.class).to(Database.class);
		bind(IEntityStore.class).to(EntityStore.class);
		bind(IIdentifierStore.class).to(IdentifierStore.class);
	}

	/**
	 * Given to always increase when any table in the database increases its
	 * version. For simplicity, it is a sum of the tables versions.
	 * 
	 * Removing a table from the sum could cause this to decrease, resulting in
	 * undefined upgrade behavior. So don't do it.
	 * 
	 */
	public static final int DATABASE_VERSION = Entities.VERSION
			+ Identifiers.VERSION;

	@Provides
	IHelper[] provideDatabaseHelpers() {
		return new IHelper[] { new Entities.DatabaseHelper(),
				new Identifiers.DatabaseHelper() };
	}

}
