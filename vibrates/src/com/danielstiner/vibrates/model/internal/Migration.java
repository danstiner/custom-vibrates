package com.danielstiner.vibrates.model.internal;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public abstract class Migration {

	public final void apply(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < version() && version() <= newVersion)
			// Upgrade
			up(db);
		else if (newVersion < version() && version() <= oldVersion)
			// Down-grade
			down(db);

	}

	protected abstract int version();

	protected abstract void up(SQLiteDatabase db);

	protected void down(SQLiteDatabase db) {
		throw new SQLiteException("Cannot downgrade database");
	}
}
