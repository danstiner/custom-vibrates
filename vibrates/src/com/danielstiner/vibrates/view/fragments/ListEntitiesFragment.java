package com.danielstiner.vibrates.view.fragments;

import roboguice.fragment.RoboListFragment;
import roboguice.inject.ContextScopedProvider;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.storage.IEntityFilter;
import com.danielstiner.vibrates.storage.IManager;
import com.danielstiner.vibrates.storage.StorageUtil;
import com.danielstiner.vibrates.view.EntitiesActivity;
import com.danielstiner.vibrates.view.OnEntitySelectedListener;
import com.danielstiner.vibrates.view.model.EntityCursorAdapter;
import com.google.inject.Inject;

public class ListEntitiesFragment extends RoboListFragment implements
		OnEntitySelectedListener {

	@Inject
	private ContextScopedProvider<IManager> mManagerProvider;

	private IManager mManager;

	@Inject
	private ContextScopedProvider<EntityCursorAdapter> mAdapterProvider;

	private EntityCursorAdapter mAdapter;

	@Inject
	private IEntityFilter mEntityFilter;

	private static final int LOADER_ID = 0;

	public ListEntitiesFragment() {

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

		mManager = mManagerProvider.get(getActivity().getApplicationContext());
		mAdapter = (mAdapter != null) ? mAdapter : mAdapterProvider.get(getActivity().getBaseContext());

		getListView().setAdapter(mAdapter);

		StorageUtil.searchIntoAdapter(LOADER_ID, getLoaderManager(), mManager,
				mEntityFilter, mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		onEntitySelected(mAdapter.getEntity(position));
	}

	@Override
	public void onEntitySelected(Entity e) {
		Activity a = getActivity();

		// Try and talk to parent activity
		if (a instanceof EntitiesActivity) {
			((EntitiesActivity) a).onEntitySelected(e);
		}
	}

	public void setType(String type) {
		if (!mEntityFilter.getType().equals(type)) {
			mEntityFilter.setType(type);
		}
	}

}
