package com.danielstiner.vibrates.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.danielstiner.vibrates.Pattern;
import com.google.inject.Inject;

public class UserSettings implements IUserSettings {

	private static final String DEFAULT_PATTERN = Pattern.NONE.toString();

	private static final String KEY_ENABLED = "enabled";
	private static final String KEY_WARN_RINGER_CHANGED = "warnonringerchange";
	private static final String KEY_KEEP_IN_SILENT_MODE = "forcesilent";
	private static final String KEY_DEFAULT_PATTERN = "defaultpattern";

	private SharedPreferences sharedPreferences;

	// @Inject
	// private Provider<Pattern> pattern_provider;

	@Inject
	UserSettings(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}

	@Override
	public Boolean enabled() {
		return sharedPreferences.getBoolean(KEY_ENABLED, false);
	}

	@Override
	public void enabled(Boolean newValue) {
		// Save new value
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(KEY_ENABLED, newValue);
		editor.commit();
	}

	@Override
	public Boolean warnOnRingerEnabled() {
		return sharedPreferences.getBoolean(KEY_WARN_RINGER_CHANGED, false);
	}

	@Override
	public Boolean keepInSilentMode() {
		return sharedPreferences.getBoolean(KEY_KEEP_IN_SILENT_MODE, false);
	}

	@Override
	public void defaultPattern(Pattern new_pattern) {
		if(new_pattern == null)
			return;

		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_DEFAULT_PATTERN, new_pattern.toString());
		editor.commit();
	}

	@Override
	public Pattern defaultPattern() {
		
		String pattern_packed = sharedPreferences.getString(KEY_DEFAULT_PATTERN, DEFAULT_PATTERN);
		
		return Pattern.fromString(pattern_packed);
	}

	@Override
	public boolean toast() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean backgroundService() {
		// TODO Auto-generated method stub
		return false;
	}

}
