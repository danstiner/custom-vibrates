package com.danielstiner.vibrates.view;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Entity.Kind;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.Vibrates;
import com.danielstiner.vibrates.view.fragments.EntityDetailFragment;
import com.danielstiner.vibrates.view.fragments.ListEntitiesFragment;
import com.danielstiner.vibrates.view.model.OnMenuSettingsClickListener;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntitiesListActivity extends RoboSherlockFragmentActivity
		implements ListEntitiesFragment.ContainerActivityInterface {

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		Map<Kind, ListEntitiesFragment> fragments = new HashMap<Entity.Kind, ListEntitiesFragment>();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public ListEntitiesFragment getItem(int i) {
			Kind k = indexToKind(i);

			if (fragments.containsKey(k))
				return fragments.get(k);

			ListEntitiesFragment frag = mEntitiesFragmentProvider.get();

			frag.setKind(k);

			fragments.put(k, frag);

			return frag;
		}

		ListEntitiesFragment getItem(Kind k) {
			return getItem(kindToIndex(k));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Contacts";
			case 1:
				return "Groups";
			case 2:
				return "App-specific";
			}
			return null;
		}

		private Kind indexToKind(int index) {
			switch (index) {
			case 0:
				return Kind.Contact;
			case 1:
				return Kind.Group;
			case 2:
				return Kind.App;
			default:
				throw new IllegalArgumentException("Unsupported section");
			}
		}

		private int kindToIndex(Kind kind) {
			switch (kind) {
			case Contact:
				return 0;
			case Group:
				return 1;
			case App:
				return 2;
			default:
				throw new IllegalArgumentException("Unsupported section");
			}
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			super.setPrimaryItem(container, position, object);

			// Update our view of which item is selected
			EntitiesListActivity.this.setKind(indexToKind(position));
		}
	}

	private static final int ACTIVITY_RESULT_ADD = 0;

	private static final String CLASSNAME = Vibrates.NS
			+ "view.EntitiesActivity";

	private static final String STATE_NAV_INDEX = CLASSNAME
			+ ".state.navigation_index";

	public static void show(Context context) {
		Intent i = new Intent(context, EntitiesListActivity.class);
		context.startActivity(i);
	}

	@Inject
	private Provider<ListEntitiesFragment> mEntitiesFragmentProvider;

	private MenuItem mMenuAddAction;

	private OnMenuItemClickListener mOnMenuAddEntityClick = new OnMenuItemClickListener() {

		public boolean onMenuItemClick(MenuItem item) {
			// Only fires for add entity button
			EntityAddActivity.show(mSelectedKind, EntitiesListActivity.this,
					EntitiesListActivity.ACTIVITY_RESULT_ADD);

			return true;
		}

	};

	private SectionsPagerAdapter mSectionsPagerAdapter;

	private Kind mSelectedKind;

	private ViewPager mViewPager;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		this.reload(this.mSelectedKind);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entities);

		// Pager for the sections
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		restoreState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		mMenuAddAction = menu.add(Menu.FIRST, Menu.FIRST, Menu.FIRST, "Add")
				.setOnMenuItemClickListener(mOnMenuAddEntityClick);
		mMenuAddAction.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		// menu.add(Menu.NONE, Menu.NONE, Menu.CATEGORY_ALTERNATIVE, "Search")
		// .setIcon(R.drawable.action_search)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		menu.add(Menu.NONE, Menu.NONE, Menu.CATEGORY_CONTAINER, "Settings")
				.setIcon(R.drawable.action_settings)
				.setOnMenuItemClickListener(
						OnMenuSettingsClickListener.getCompatInstance(this))
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return true;
	}

	@Override
	public void onEntitySelected(Entity e) {
		EntityDetailFragment editEntity = (EntityDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.entity_fragment);

		if (editEntity == null || !editEntity.isInLayout()) {
			EntityDetailActivity.show(e, (Context) this);
		} else {
			editEntity.onEntitySelected(e);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save selected navigation list element
		if (mSelectedKind != null)
			outState.putString(STATE_NAV_INDEX, mSelectedKind.toString());
	}

	private void reload(Kind kind) {
		mSectionsPagerAdapter.getItem(kind).reload();
	}

	private void restoreState(Bundle savedInstanceState) {
		if (savedInstanceState == null)
			return;

		// Restore selected navigation list element
		// if (savedInstanceState.containsKey(STATE_NAV_INDEX))
		// getActionBar().setSelectedNavigationItem(
		// savedInstanceState.getInt(STATE_NAV_INDEX));
	}

	private void setKind(Entity.Kind kind) {
		this.mSelectedKind = kind;

		updateAddAction(kind);
	}

	private void updateAddAction(Kind kind) {

		if (mMenuAddAction == null) {
			// Do nothing
		} else if (kind == Kind.Contact) {
			mMenuAddAction.setIcon(R.drawable.social_add_person);
			mMenuAddAction.setTitle("Add Contact");
		} else if (kind == Kind.Group) {
			mMenuAddAction.setIcon(R.drawable.social_add_group);
			mMenuAddAction.setTitle("Add Group");
		} else if (kind == Kind.App) {
			mMenuAddAction.setIcon(R.drawable.av_add_to_queue);
			mMenuAddAction.setTitle("Add App");
		}
	}
}
