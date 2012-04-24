package com.danielstiner.vibrates.view;

import roboguice.activity.RoboActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.view.model.OnMenuSettingsClickListener;

public class EntityDetail extends RoboActivity {

	public static void show(Entity e, Context context) {
		Intent i = new Intent(context, EntityDetail.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			EntitiesActivity.show(this);
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, Menu.NONE, Menu.FIRST, "Delete")
				.setIcon(R.drawable.content_discard)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(Menu.NONE, Menu.NONE, Menu.CATEGORY_CONTAINER, "Settings")
				.setIcon(R.drawable.action_settings)
				.setOnMenuItemClickListener(
						OnMenuSettingsClickListener.getInstance(this))
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		return super.onCreateOptionsMenu(menu);
	}
}
