package com.danielstiner.vibrates.view;

import roboguice.activity.RoboActivity;
import android.content.Context;
import android.os.Bundle;

import com.danielstiner.vibrates.settings.IUserSettings;
import com.google.inject.Inject;

public class LaunchActivity extends RoboActivity {

	@Inject
	private IUserSettings mSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Show contact/welcome view if its the launch of application
		// Otherwise assume the back button was pressed and exit
		if (savedInstanceState == null) {

			// On first launch, instead show the welcome screen
			if (!mSettings.welcomed()) {
				WelcomeActivity.show((Context) this);
			} else {
				EntitiesListActivity.show((Context) this);
			}
		} else {
			finish();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		finish();
	}

}
