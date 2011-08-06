package com.danielstiner.vibrates.settings;

import android.os.Bundle;

import com.danielstiner.vibrates.R;

import roboguice.activity.RoboPreferenceActivity;

public class Preferences extends RoboPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preferences);
	}
}
