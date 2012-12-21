package com.danielstiner.vibrates.view.model;

import android.content.Context;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

import com.danielstiner.vibrates.settings.Preferences;

public class OnMenuSettingsClickListener implements
		android.view.MenuItem.OnMenuItemClickListener {

	private Context mContext;

	public OnMenuSettingsClickListener(Context context) {
		this.mContext = context;
	}

	public static OnMenuItemClickListener getInstance(Context context) {
		return new OnMenuSettingsClickListener(context);
	}

	@Override
	public boolean onMenuItemClick(MenuItem arg0) {
		Preferences.show(mContext);

		return true;
	}

}
