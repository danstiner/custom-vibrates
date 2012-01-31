package com.danielstiner.vibrates.settings;

import com.danielstiner.vibrates.Pattern;

public interface IUserSettings {

	Boolean enabled();

	Boolean warnOnRingerEnabled();

	Boolean keepInSilentMode();

	Pattern defaultPattern(String string);

	void defaultPattern(long[] newPattern);

	void enabled(Boolean newValue);

	Pattern defaultPattern();

	boolean toast();
}
