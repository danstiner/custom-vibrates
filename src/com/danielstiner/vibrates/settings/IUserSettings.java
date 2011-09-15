package com.danielstiner.vibrates.settings;

public interface IUserSettings {

	Boolean enabled();

	Boolean warnOnRingerEnabled();
	
	Boolean keepInSilentMode();

	long[] defaultPattern();

	void defaultPattern(long[] newPattern);

	void enabled(Boolean newValue);
}
