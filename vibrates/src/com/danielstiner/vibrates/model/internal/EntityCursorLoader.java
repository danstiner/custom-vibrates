package com.danielstiner.vibrates.model.internal;

import android.content.Context;
import android.support.v4.content.CursorLoader;

import com.danielstiner.vibrates.model.IEntityFilter;

public class EntityCursorLoader extends CursorLoader implements
		IEntityFilter.IEntityFilterProviderTarget {

	public EntityCursorLoader(Context context) {
		super(context);
	}

}
