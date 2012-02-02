package com.danielstiner.vibrates.storage.internal;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

public class EntityLoaderCallbacks implements LoaderCallbacks<Cursor> {

	private ISearchCallback mCallback;

	public EntityLoaderCallbacks(ISearchCallback callback) {
		mCallback = callback;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;

		// String[] projection = { TutListDatabase.ID, TutListDatabase.COL_TITLE
		// };
		//
		// CursorLoader cursorLoader = new CursorLoader(getActivity(),
		// TutListProvider.CONTENT_URI, projection, null, null, null);
		// return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mCallback.update(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCallback.update(null);
	}

}
