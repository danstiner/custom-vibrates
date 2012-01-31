package com.danielstiner.vibrates.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.danielstiner.vibrates.Pattern;
import com.google.inject.Inject;

public class UserSettings implements IUserSettings {

	// private static final String DEFAULT_PATTERN = "0,0";

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

	// @Override
	// public Pattern defaultPattern(String string, INotificationConstraints
	// notifyConstraints) {
	// String pattern_packed = sharedPreferences.getString(KEY_DEFAULT_PATTERN,
	// DEFAULT_PATTERN);
	//
	// if(pattern_packed == null)
	// return null;
	//
	// String[] pattern_parts = pattern_packed.split(",");
	// long[] pattern = new long[pattern_parts.length];
	//
	// try {
	// for(int i=0; i<pattern.length; i++)
	// pattern[i] = Long.parseLong(pattern_parts[i].trim());
	// } catch(Exception e) {
	// Ln.d(e, "Could not parse pattern '%s' from database.", pattern_packed);
	// }
	//
	// return pattern_provider.get().pattern(pattern);
	// }

	@Override
	public Pattern defaultPattern() {
		// return defaultPattern(null, null);
		// TODO
		return null;
	}

	@Override
	public void defaultPattern(long[] new_pattern) {
		String pattern_packed = "";

		// Concatenate pattern values into csv
		for (int i = 0; i < new_pattern.length; i++)
			pattern_packed += "," + new_pattern[i];

		// Strip preceding comma
		pattern_packed = pattern_packed.substring(1);

		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_DEFAULT_PATTERN, pattern_packed);
		editor.commit();
	}

	@Override
	public Pattern defaultPattern(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean toast() {
		// TODO Auto-generated method stub
		return false;
	}

}
