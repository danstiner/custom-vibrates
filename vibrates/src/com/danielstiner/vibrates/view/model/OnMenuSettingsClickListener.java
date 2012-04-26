package com.danielstiner.vibrates.view.model;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.danielstiner.vibrates.settings.Preferences;

import android.content.Context;

public class OnMenuSettingsClickListener implements
		com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener {

	private Context mContext;

	public OnMenuSettingsClickListener(Context context) {
		this.mContext = context;
	}

	public static OnMenuItemClickListener getInstance(
			Context context) {
		return new OnMenuSettingsClickListener(context);
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		Preferences.show(mContext);

		return true;
	}

}
