package com.danielstiner.vibrates.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;

public class ManagersModule extends AbstractModule {
	
	private static final String DATABASE_NAME = "vibrates";
	
	
	static final String DATABASE_NAME_KEY = "database_name";

	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named(DATABASE_NAME_KEY)).to(DATABASE_NAME);		
	}
	
	@Provides
	IDatabaseHelper[] provideDatabaseHelpers()
	{
		return new IDatabaseHelper[] {  };
	}

}
