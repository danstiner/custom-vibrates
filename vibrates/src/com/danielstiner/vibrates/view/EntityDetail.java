package com.danielstiner.vibrates.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.view.model.OnMenuSettingsClickListener;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;

public class EntityDetail extends RoboSherlockActivity {

	public static void show(Entity e, Context context) {
		Intent i = new Intent(context, EntityDetail.class);
		context.startActivity(i);
	}

	private Entity mEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Move into tabs if we get more than one
		setContentView(R.layout.entity_detail);

		if (mEntity.getKind() == Entity.Kind.Contact) {
			setTitle("Contact Detail");
		}

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.addTab(actionBar.newTab().setText("Pattern"));

	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
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
