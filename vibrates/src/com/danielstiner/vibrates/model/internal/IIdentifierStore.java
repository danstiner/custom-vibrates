package com.danielstiner.vibrates.model.internal;

import android.database.Cursor;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Identifier;

public interface IIdentifierStore {

	Cursor getOrphans();

	Cursor search(Entity owner);

	Cursor search(Entity owner, Identifier kind);

	Cursor search(Identifier identifier);

	void add(Entity to, Identifier identifier);

	Identifier fromCursor(Cursor c);

	int removeAll(Entity entity);

	Entity entityFromCursor(Cursor c);

}