package com.danielstiner.vibrates.view;

import roboguice.activity.RoboFragmentActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ArrayAdapter;

import android.app.ActionBar;
import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Entity.Kind;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.Vibrates;
import com.danielstiner.vibrates.view.fragments.EditEntity;
import com.danielstiner.vibrates.view.fragments.IListEntitiesFragment;
import com.danielstiner.vibrates.view.fragments.ListEntitiesFragment;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntitiesActivity extends RoboFragmentActivity implements
		OnEntitySelectedListener {

	private static final String CLASSNAME = Vibrates.NS + "view.Entities";

	private static final int CONTACTS_TAB_POS = 0;

	private static final int GROUPS_TAB_POS = 1;

	private static final int APPS_TAB_POS = 2;

	private static final String ENTITIES_TAG = "entities";

	private static final String STATE_NAV_INDEX = CLASSNAME
			+ ".state.navigation_index";

	protected static final int ACTIVITY_RESULT_ADD = 0;

	private IListEntitiesFragment mEntitiesFragment;

	@Inject
	private Provider<ListEntitiesFragment> mEntitiesFragmentProvider;

	private final ActionBar.OnNavigationListener mNavListener = new ActionBar.OnNavigationListener() {

		public boolean onNavigationItemSelected(int itemPosition, long itemId) {

			switch (itemPosition) {
			case CONTACTS_TAB_POS:
				setKind(Entity.Kind.Contact);
				break;
			case GROUPS_TAB_POS:
				setKind(Entity.Kind.Group);
				break;
			case APPS_TAB_POS:
				setKind(Entity.Kind.App);
				break;
			}

			return true;
		}

	};

	private IListEntitiesFragment loadChildFragment() {
		if (mEntitiesFragment == null
				|| mEntitiesFragment != getSupportFragmentManager()
						.findFragmentById(R.id.fragment_container)) {

			if (mEntitiesFragment == null)
				mEntitiesFragment = (ListEntitiesFragment) getSupportFragmentManager()
						.findFragmentByTag(ENTITIES_TAG);

			if (mEntitiesFragment == null) {
				mEntitiesFragment = mEntitiesFragmentProvider.get();

				FragmentTransaction transact = getSupportFragmentManager()
						.beginTransaction();

				transact.replace(R.id.fragment_container,
						(ListEntitiesFragment) mEntitiesFragment, ENTITIES_TAG);

				transact.commit();
			}
		}

		// Should always have a fragment by this time
		return mEntitiesFragment;
	}

	private OnMenuItemClickListener mOnMenuAddClick = new OnMenuItemClickListener() {

		public boolean onMenuItemClick(MenuItem item) {
			// Only fires for add entity button
			EntityAddActivity.show(mSelectedKind, EntitiesActivity.this, EntitiesActivity.ACTIVITY_RESULT_ADD);
			
			return true;
		}
		
	};

	private ActionBar mActionBar;

	private Entity mSelectedEntity;

	private Kind mSelectedKind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entities);

		mActionBar = getActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO
				| ActionBar.DISPLAY_SHOW_HOME);

		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(this,
				R.array.entity_dropdown_list,
				android.R.layout.simple_dropdown_item_1line);

		list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		getActionBar().setListNavigationCallbacks(list, mNavListener);

		restoreState(savedInstanceState);

	}

	private void restoreState(Bundle savedInstanceState) {
		if (savedInstanceState == null)
			return;

		// Restore selected navigation list element
		if (savedInstanceState.containsKey(STATE_NAV_INDEX))
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_NAV_INDEX));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save selected navigation list element
		outState.putInt(STATE_NAV_INDEX, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Add")
				.setIcon(R.drawable.ic_action_add)
				.setOnMenuItemClickListener(mOnMenuAddClick)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add("Search")
				.setIcon(R.drawable.ic_action_search)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	public void onEntitySelected(Entity e) {
		mSelectedEntity = e;

		EditEntity editEntity = (EditEntity) getSupportFragmentManager()
				.findFragmentById(R.id.entity_edit_fragment);

		if (editEntity == null || !editEntity.isInLayout()) {
			EntityDetail.show(e, getApplicationContext());
		} else {
			editEntity.onEntitySelected(e);
		}
	}
	
	private void setKind(Entity.Kind kind) {
		
		IListEntitiesFragment entities_frag = loadChildFragment();
		
		this.mSelectedKind = kind;
		
		if(entities_frag != null)
			entities_frag.setKind(kind);
	}

	public static void show(Context context) {
		// TODO Auto-generated method stub
		
	}
}
