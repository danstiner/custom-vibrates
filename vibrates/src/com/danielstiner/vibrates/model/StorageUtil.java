package com.danielstiner.vibrates.model;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.CursorAdapter;

import com.danielstiner.vibrates.model.internal.ISearchCallback;

public class StorageUtil {

	public static void searchIntoAdapter(int loaderId,
			LoaderManager loaderManager, IManager manager,
			IEntityFilter entityFilter, CursorAdapter adapter) {
		manager.searchEntities(loaderId, loaderManager, entityFilter,
				new SearchCallback(adapter));
	}

	private static class SearchCallback implements ISearchCallback {

		private CursorAdapter mAdapter;

		public SearchCallback(CursorAdapter adapter) {
			mAdapter = adapter;
		}

		@Override
		public void update(Cursor cursor) {
			if (mAdapter != null)
				mAdapter.swapCursor(cursor);
		}
	}

}
