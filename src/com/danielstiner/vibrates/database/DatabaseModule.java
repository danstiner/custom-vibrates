package com.danielstiner.vibrates.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class DatabaseModule extends AbstractModule {
	
	private static final String DATABASE_NAME = "vibrates";
	
	
	static final String DATABASE_NAME_KEY = "database_name";

	@Override
	protected void configure() {
		bind(IDatabase.class).to(VibratesDatabase.class);
		bindConstant().annotatedWith(Names.named(DATABASE_NAME_KEY)).to(DATABASE_NAME);
		bind(IDatabaseHelper[].class);
	}
	
	@Provides
	IDatabaseHelper[] provideDatabaseHelpers()
	{
		return new IDatabaseHelper[] {
			new EntityManager.Helper(),
			new IdentifierManager.Helper()
		};
	}

}
