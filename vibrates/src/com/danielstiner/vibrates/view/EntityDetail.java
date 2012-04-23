package com.danielstiner.vibrates.view;

import roboguice.activity.RoboActivity;
import android.content.Context;
import android.content.Intent;

import com.danielstiner.vibrates.Entity;

public class EntityDetail extends RoboActivity {

	public static void show(Entity e, Context context) {
		Intent i = new Intent(context, EntityDetail.class);
		context.startActivity(i);
	}
}
