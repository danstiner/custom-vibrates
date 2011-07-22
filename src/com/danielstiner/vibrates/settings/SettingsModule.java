package com.danielstiner.vibrates.settings;

import com.google.inject.AbstractModule;

public class SettingsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IUserSettings.class).to(UserSettings.class);
	}

}
