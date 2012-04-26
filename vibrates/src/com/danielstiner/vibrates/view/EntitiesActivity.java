package com.danielstiner.vibrates.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Entity.Kind;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.Vibrates;
import com.danielstiner.vibrates.view.fragments.EditEntity;
import com.danielstiner.vibrates.view.fragments.IListEntitiesFragment;
import com.danielstiner.vibrates.view.fragments.ListEntitiesFragment;
import com.danielstiner.vibrates.view.model.OnEntitySelectedListener;
import com.danielstiner.vibrates.view.model.OnMenuSettingsClickListener;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntitiesActivity extends RoboSherlockFragmentActivity implements
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

	private OnMenuItemClickListener mOnMenuAddEntityClick = new OnMenuItemClickListener() {

		public boolean onMenuItemClick(MenuItem item) {
			// Only fires for add entity button
			EntityAddActivity.show(mSelectedKind, EntitiesActivity.this,
					EntitiesActivity.ACTIVITY_RESULT_ADD);

			return true;
		}

	};

	private ActionBar mActionBar;

	private Entity mSelectedEntity;

	private Kind mSelectedKind;

	private MenuItem mMenuAddAction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entities);

		mActionBar = getSupportActionBar();
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO
				| ActionBar.DISPLAY_SHOW_HOME);

		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(this,
				R.array.entity_dropdown_list,
				android.R.layout.simple_dropdown_item_1line);

		list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mActionBar.setListNavigationCallbacks(list, mNavListener);

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

		mMenuAddAction = menu.add(Menu.FIRST, Menu.FIRST, Menu.FIRST, "Add")
				.setOnMenuItemClickListener(mOnMenuAddEntityClick);
		mMenuAddAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(Menu.NONE, Menu.NONE, Menu.CATEGORY_ALTERNATIVE, "Search")
				.setIcon(R.drawable.action_search)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM);

		menu.add(Menu.NONE, Menu.NONE, Menu.CATEGORY_CONTAINER, "Settings")
				.setIcon(R.drawable.action_settings)
				.setOnMenuItemClickListener(OnMenuSettingsClickListener.getInstance(this))
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_NEVER);

		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		this.refresh();
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void refresh() {
		// Forces refresh
		this.setKind(mSelectedKind);
	}

	@Override
	public void onEntitySelected(Entity e) {
		mSelectedEntity = e;

		EditEntity editEntity = (EditEntity) getSupportFragmentManager()
				.findFragmentById(R.id.entity_edit_fragment);

		if (editEntity == null || !editEntity.isInLayout()) {
			EntityDetail.show(e, (Context) this);
		} else {
			editEntity.onEntitySelected(e);
		}
	}

	private void setKind(Entity.Kind kind) {

		IListEntitiesFragment entities_frag = loadChildFragment();

		this.mSelectedKind = kind;
		
		updateAddAction();

		if (entities_frag != null)
			entities_frag.setKind(kind);
	}
	
	private void updateAddAction() {

		if (mMenuAddAction == null) {
			// Do nothing
		} else if (mSelectedKind == Kind.Contact) {
			mMenuAddAction.setIcon(R.drawable.social_add_person);
			mMenuAddAction.setTitle("Add Contact");
		} else if (mSelectedKind == Kind.Group) {
			mMenuAddAction.setIcon(R.drawable.social_add_group);
			mMenuAddAction.setTitle("Add Group");
		} else if (mSelectedKind == Kind.App) {
			mMenuAddAction.setIcon(R.drawable.av_add_to_queue);
			mMenuAddAction.setTitle("Add App");
		}
	}

	public static void show(Context context) {
		Intent i = new Intent(context, EntitiesActivity.class);
		context.startActivity(i);
	}
}
