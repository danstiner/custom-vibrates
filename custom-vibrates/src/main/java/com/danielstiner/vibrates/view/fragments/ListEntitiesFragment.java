package com.danielstiner.vibrates.view.fragments;

import roboguice.fragment.RoboListFragment;
import roboguice.inject.ContextScopedProvider;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.model.IEntityFilter;
import com.danielstiner.vibrates.model.StorageUtil;
import com.danielstiner.vibrates.model.internal.EntityFilter;
import com.danielstiner.vibrates.view.model.EntityCursorAdapter;
import com.danielstiner.vibrates.view.model.OnEntitySelectedListener;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ListEntitiesFragment extends RoboListFragment implements
		IListEntitiesFragment, OnEntitySelectedListener {

	public interface ContainerActivityInterface {
		public void onEntitySelected(Entity e);
	}

	private static final int CONTEXTMENU_DELETE = 1;

	@Inject
	private ContextScopedProvider<IDataModel> mManagerProvider;

	private IDataModel mManager;

	@Inject
	private ContextScopedProvider<EntityCursorAdapter> mAdapterProvider;

	private EntityCursorAdapter mAdapter;

	//@Inject
	private IEntityFilter mEntityFilter = new EntityFilter();

	@Inject
	private Provider<Application> mContextProvider;

	private boolean stale = true;

	public ListEntitiesFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.entity_list, null);
	}

	// Called after onCreateView
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Injects members through roboguice
		super.onViewCreated(view, savedInstanceState);

		registerForContextMenu(getListView());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mManager = (mManager != null) ? mManager : mManagerProvider
				.get(mContextProvider.get());
		mAdapter = (mAdapter != null) ? mAdapter : mAdapterProvider
				.get(mContextProvider.get());

		getListView().setAdapter(mAdapter);
		
		reloadIfStale();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Force correct container type
		getContainer();
	}

	private ContainerActivityInterface getContainer() {

		Activity activity = getActivity();

		if (activity == null)
			return null;

		try {
			return (ContainerActivityInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ContainerActivityInterface");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		onEntitySelected(mAdapter.getEntity(position));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CONTEXTMENU_DELETE, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		switch (item.getItemId()) {
		case CONTEXTMENU_DELETE:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();

			onRemoveEntity(mAdapter.getEntity(info.position));
			break;
		}

		return super.onContextItemSelected(item);
	}

	private void onRemoveEntity(Entity entity) {
		mManager.remove(entity);

		reload();
	}

	@Override
	public void onEntitySelected(Entity e) {

		// Try and talk to parent activity
		ContainerActivityInterface cai = getContainer();
		if (cai != null)
			cai.onEntitySelected(e);

	}

	@Override
	public void setKind(Entity.Kind kind) {
		mEntityFilter.setKind(kind);

		reload();
	}

	public void reload() {
		if (getActivity() != null && mManager != null && mEntityFilter.getKind() != null) {
			StorageUtil.searchIntoAdapter(getLoaderManager(), mManager,
					mEntityFilter, mAdapter);
			stale = false;
		} else {
			stale = true;
		}
	}

	private void reloadIfStale() {
		if (stale) {
			reload();
		}
	}

}
