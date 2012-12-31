package com.danielstiner.vibrates.model;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.CursorAdapter;
import android.widget.QuickContactBadge;

import com.danielstiner.vibrates.model.internal.ISearchCallback;

public class StorageUtil {

	public static void searchIntoAdapter(LoaderManager loaderManager,
			IDataModel manager, IEntityFilter entityFilter,
			CursorAdapter adapter) {		
		manager.searchEntities(loaderManager, entityFilter, new SearchCallback(
				adapter));
	}

	public static void createContactFromUri(IDataModel model, Uri contact_path) {
		model.createFromContactUri(contact_path);
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

	public static void assignPicture(QuickContactBadge mQuickContactBadge,
			IDataModel model) {
		// TODO Auto-generated method stub
		
	}

}
