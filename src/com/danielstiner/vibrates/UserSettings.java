package com.danielstiner.vibrates;

import android.content.SharedPreferences;

import com.google.inject.Inject;

public class UserSettings implements IUserSettings {

	
	private SharedPreferences sharedPreferences;

	@Inject
	UserSettings(SharedPreferences sharedPreferences)
	{
		this.sharedPreferences = sharedPreferences;
	}

	@Override
	public Boolean enabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
