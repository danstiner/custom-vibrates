package com.danielstiner.vibrates.view.model;

import com.danielstiner.vibrates.settings.Preferences;
import com.danielstiner.vibrates.view.EntitiesActivity;

import android.content.Context;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class OnMenuSettingsClickListener implements OnMenuItemClickListener {
	
	private Context mContext;

	public OnMenuSettingsClickListener(Context context) {
		this.mContext = context;
	}

	public static OnMenuSettingsClickListener getInstance(Context context)
	{
		return new OnMenuSettingsClickListener(context);
	}

	@Override
	public boolean onMenuItemClick(MenuItem paramMenuItem) {
		Preferences.show(mContext);

		return true;
	}

}
