package com.danielstiner.vibrates.settings;

import roboguice.activity.RoboPreferenceActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.danielstiner.vibrates.R;

public class Preferences extends RoboPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			Preferences.this.finish();
		}
		
		return super.onOptionsItemSelected(item);
	}

	public static void show(Context context) {
		Intent i = new Intent(context, Preferences.class);
		context.startActivity(i);
	}
}
