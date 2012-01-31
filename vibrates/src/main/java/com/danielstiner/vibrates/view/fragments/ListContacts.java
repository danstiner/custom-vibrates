package com.danielstiner.vibrates.view.fragments;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;

import roboguice.fragment.RoboListFragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListContacts extends RoboListFragment {

	private static final int LOADER_ID = 0;
	private final LoaderCallbacks<Void> mLoaderCallbacks = new LoaderCallbacks<Void>() {

		@Override
		public Loader<Void> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onLoadFinished(Loader<Void> arg0, Void arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLoaderReset(Loader<Void> arg0) {
			// TODO Auto-generated method stub

		}

	};

	public ListContacts() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_entity_list, null);
	}

	// Called after onCreateView
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Injects members through roboguice
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		if (mAdapter == null) {
			mItems = new ArrayList<String>();
			mAdapter = new MyAdapter(getActivity(), mItems);
		}
		getListView().setAdapter(mAdapter);

		getLoaderManager().initLoader(LOADER_ID, null, mLoaderCallbacks);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}

}
