package com.danielstiner.vibrates.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class LaunchActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO On first launch, instead show the welcome screen
		EntitiesActivity.show((Context) this);
	}

}
