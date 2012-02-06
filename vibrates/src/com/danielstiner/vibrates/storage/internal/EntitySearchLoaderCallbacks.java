package com.danielstiner.vibrates.storage.internal;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.danielstiner.vibrates.storage.IEntityFilter;

public class EntitySearchLoaderCallbacks implements LoaderCallbacks<Cursor> {

	private ISearchCallback mCallback;
	private Context mContext;
	private IEntityFilter mFilter;
	private EntityCursorLoader mCursorLoader;

	EntitySearchLoaderCallbacks(Context context) {
		mContext = context;
	}

	public EntitySearchLoaderCallbacks setCallback(ISearchCallback callback) {
		mCallback = callback;
		return this;
	}

	public EntitySearchLoaderCallbacks setFilter(IEntityFilter filter) {
		mFilter = filter;

		if (mCursorLoader != null)
			mFilter.apply(mCursorLoader);

		return this;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		mCursorLoader = new EntityCursorLoader(mContext);

		if (mFilter != null)
			mFilter.apply(mCursorLoader);

		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (mCallback != null)
			mCallback.update(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (mCallback != null)
			mCallback.update(null);
	}

}
