package com.danielstiner.vibrates.storage;

import com.danielstiner.vibrates.storage.internal.ISearchCallback;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.CursorAdapter;

public class StorageUtil {

	public static void searchIntoAdapter(int loaderId, LoaderManager loaderManager, IManager mManager,
			IEntityFilter entityFilter, CursorAdapter adapter) {
		mManager.searchEntities(loaderId, loaderManager, entityFilter, new SearchCallback(adapter));
	}
	
	private static class SearchCallback implements ISearchCallback {
		
		private CursorAdapter mAdapter;
		
		public SearchCallback(CursorAdapter adapter) {
			mAdapter = adapter;
		}
		
		
		@Override
		public void update(Cursor cursor) {
			if(mAdapter != null)
				mAdapter.swapCursor(cursor);
		}
	}

}
