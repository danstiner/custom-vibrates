package com.danielstiner.vibrates.settings;

import roboguice.activity.RoboPreferenceActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.danielstiner.vibrates.R;

public class Preferences extends RoboPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	public static void show(Context context) {
		Intent i = new Intent(context, Preferences.class);
		context.startActivity(i);
	}
}
