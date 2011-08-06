package com.danielstiner.vibrates.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.inject.Inject;

public class UserSettings implements IUserSettings {

	
	private static final String KEY_ENABLED = "enabled";
	private static final String KEY_WARN_RINGER_CHANGED = "warnonringerchange";
	private static final String KEY_KEEP_IN_SILENT_MODE = "forcesilent";
	private SharedPreferences sharedPreferences;

	@Inject
	UserSettings(SharedPreferences sharedPreferences)
	{
		this.sharedPreferences = sharedPreferences;
	}

	@Override
	public Boolean enabled() {
		return sharedPreferences.getBoolean(KEY_ENABLED, false);
	}
	
	@Override
	public Boolean warnOnRingerEnabled() {
		return sharedPreferences.getBoolean(KEY_WARN_RINGER_CHANGED, false);
	}

	@Override
	public Boolean keepInSilentMode() {
		return sharedPreferences.getBoolean(KEY_KEEP_IN_SILENT_MODE, false);
	}

}
