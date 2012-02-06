package com.danielstiner.vibrates.storage;

import android.net.Uri;

import com.danielstiner.vibrates.Entity;

public interface IEntityFilter {

	IEntityFilter setKind(Entity.Kind kind);

	Entity.Kind getKind();

	int getLoaderId();

	boolean isInitialized();

	void initialize();

	void apply(IEntityFilterProviderTarget target);

	public interface IEntityFilterProviderTarget {
		void setUri(Uri uri);

		void setSelection(String selection);

		void setSelectionArgs(String[] selectionArgs);

		void setSortOrder(String sortOrder);

		void setProjection(String[] projection);
	}
}
