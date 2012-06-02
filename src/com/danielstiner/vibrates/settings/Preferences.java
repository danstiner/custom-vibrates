package com.danielstiner.vibrates.settings;

import roboguice.activity.RoboPreferenceActivity;
import android.os.Bundle;

import com.danielstiner.vibrates.R;

public class Preferences extends RoboPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
