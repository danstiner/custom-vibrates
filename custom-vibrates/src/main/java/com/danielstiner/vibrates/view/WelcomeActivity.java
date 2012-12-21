package com.danielstiner.vibrates.view;

import roboguice.activity.RoboActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.Vibrates;

public class WelcomeActivity extends RoboActivity {

	private static final String CLASSNAME = Vibrates.NS
			+ "view.WelcomeActivity";

	public static void show(Context context) {
		Intent i = new Intent(context, WelcomeActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.welcome);
	}
}
