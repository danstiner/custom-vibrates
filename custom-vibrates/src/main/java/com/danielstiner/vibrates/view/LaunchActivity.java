package com.danielstiner.vibrates.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO On first launch, instead show the welcome screen

		// Show contact view if its the launch of application
		// Otherwise assume the back button was pressed and exit
		if (savedInstanceState == null)
			EntitiesActivity.show((Context) this);
		else
			finish();
	}
	
	@Override
	protected void onResume() {
		finish();
	}

}
