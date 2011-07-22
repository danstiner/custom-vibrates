package com.danielstiner.vibrates;

import java.util.List;

import com.danielstiner.vibrates.database.DatabaseModule;
import com.danielstiner.vibrates.database.ManagersModule;
import com.danielstiner.vibrates.notify.NotifyModule;
import com.danielstiner.vibrates.settings.SettingsModule;
import com.google.inject.Module;

import roboguice.application.RoboApplication;

public class Vibrates extends RoboApplication {
	/**  */
	public static final String NS = "com.danielstiner.vibrates";

	@Override
	protected void addApplicationModules(List<Module> modules) {
        modules.add(new DatabaseModule());
        modules.add(new ManagersModule());
        modules.add(new NotifyModule());
        modules.add(new SettingsModule());
    }
}
