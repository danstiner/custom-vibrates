package com.danielstiner.vibrates.views;

import com.danielstiner.vibrates.R;

import android.os.Bundle;
import android.view.MenuItem;
import roboguice.activity.RoboListActivity;

public abstract class CoreListActivity extends RoboListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(getContentView());
		
		// Register for context events from the list
		registerForContextMenu(getListView());
		
		fillList();
		
		initEmptyView();
	}
	
	protected abstract int getContentView();
	
	protected abstract void initEmptyView();
	
	protected abstract void fillList();
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			// TODO Something intelligents
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
