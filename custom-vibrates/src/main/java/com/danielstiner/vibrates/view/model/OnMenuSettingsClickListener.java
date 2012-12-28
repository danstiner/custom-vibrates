package com.danielstiner.vibrates.view.model;

import android.content.Context;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.danielstiner.vibrates.settings.Preferences;

public class OnMenuSettingsClickListener implements
		OnMenuItemClickListener, android.view.MenuItem.OnMenuItemClickListener {

	private Context mContext;

	public OnMenuSettingsClickListener(Context context) {
		this.mContext = context;
	}
	
	public static OnMenuItemClickListener getCompatInstance(Context context) {
		return new OnMenuSettingsClickListener(context);
	}

	public static android.view.MenuItem.OnMenuItemClickListener getInstance(Context context) {
		return new OnMenuSettingsClickListener(context);
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		Preferences.show(mContext);
		return false;
	}

	@Override
	public boolean onMenuItemClick(android.view.MenuItem arg0) {
		Preferences.show(mContext);
		return false;
	}

}
