package com.danielstiner.vibrates.settings;

import com.danielstiner.vibrates.Pattern;

public interface IUserSettings {

	Boolean enabled();

	Boolean warnOnRingerEnabled();

	Boolean keepInSilentMode();

	void enabled(Boolean newValue);

	Pattern defaultPattern();

	boolean toast();

	boolean backgroundService();

	void defaultPattern(Pattern new_pattern);
}
