package com.danielstiner.vibrates.storage.internal;

import android.content.Context;
import android.support.v4.content.CursorLoader;

import com.danielstiner.vibrates.storage.IEntityFilter;

public class EntityCursorLoader extends CursorLoader implements
		IEntityFilter.IEntityFilterProviderTarget {

	public EntityCursorLoader(Context context) {
		super(context);
	}

}
