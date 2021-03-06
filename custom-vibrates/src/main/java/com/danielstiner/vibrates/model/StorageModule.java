package com.danielstiner.vibrates.model;

import com.danielstiner.vibrates.model.internal.DataModel;
import com.danielstiner.vibrates.model.internal.Database;
import com.danielstiner.vibrates.model.internal.EntityFilter;
import com.danielstiner.vibrates.model.internal.EntityStore;
import com.danielstiner.vibrates.model.internal.IDatabase;
import com.danielstiner.vibrates.model.internal.IDatabase.IHelper;
import com.danielstiner.vibrates.model.internal.IEntityStore;
import com.danielstiner.vibrates.model.internal.IIdentifierStore;
import com.danielstiner.vibrates.model.internal.IdentifierStore;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class StorageModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IDataModel.class).to(DataModel.class);
		bind(IEntityFilter.class).to(EntityFilter.class);
		bind(IDatabase.class).to(Database.class);
		bind(IEntityStore.class).to(EntityStore.class);
		bind(IIdentifierStore.class).to(IdentifierStore.class);
	}

	@Provides
	IHelper[] provideDatabaseHelpers() {
		return new IHelper[] { new Entities.DatabaseHelper(),
				new Identifiers.DatabaseHelper() };
	}

}
