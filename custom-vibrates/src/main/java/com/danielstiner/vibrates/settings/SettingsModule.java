package com.danielstiner.vibrates.settings;

import com.google.inject.AbstractModule;

public class SettingsModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IUserSettings.class).to(UserSettings.class);

		// Needed so roboguice grabs the private preferences instance used by
		// PreferenceScreen
//		bindConstant().annotatedWith(SharedPreferencesName.class).to(
//				Vibrates.NS + "_preferences");
	}

}
