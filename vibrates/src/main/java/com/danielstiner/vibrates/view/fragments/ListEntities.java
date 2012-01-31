package com.danielstiner.vibrates.view.fragments;

import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ListEntities extends RoboListFragment {
	
	public interface OnEntitySelectedListener {
		public void onEntitySelected(Entity e);
	}

	private static final String TAB_CONTACTS_TAG = "contacts";

	private static final String TAB_GROUPS_TAG = "groups";

	private static final int TAB_CONTACTS_ID = 0;

	private static final int TAB_GROUPS_ID = 1;

	private static final int TAB_CONTACTS_CONTENT = R.id.tab_contacts;

	private static final int TAB_GROUPS_CONTENT = R.id.tab_groups;

	@InjectView(android.R.id.tabhost)
	private TabHost mTabHost;

	@InjectView(android.R.id.tabs)
	private ViewGroup mTabs;

	@Inject
	private Provider<ListContacts> mProvideListContactsFragment;

	@Inject
	private Provider<ListGroups> mProvideListGroupsFragment;

	private int mCurrentTabId;

	private final OnTabChangeListener mTabChangeListener = new OnTabChangeListener() {

		@Override
		public void onTabChanged(String tabId) {
			if (TAB_CONTACTS_TAG.equals(tabId)) {
				updateTab(TAB_CONTACTS_TAG);
				mCurrentTabId = TAB_CONTACTS_ID;
			} else if (TAB_GROUPS_TAG.equals(tabId)) {
				updateTab(TAB_CONTACTS_TAG);
				mCurrentTabId = TAB_GROUPS_ID;
			}
		}
	};

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

		initTabs();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mTabHost.setOnTabChangedListener(mTabChangeListener);
		mTabHost.setCurrentTab(mCurrentTabId);

		// Fill tabs
		updateTab(tagForTabId(mCurrentTabId));
	}

	private void initTabs() {
		mTabHost.setup();
		mTabHost.addTab(buildTab(TAB_CONTACTS_TAG, R.string.tab_contacts_label));
		mTabHost.addTab(buildTab(TAB_GROUPS_TAG, R.string.tab_groups_label));
	}

	private TabSpec buildTab(String tag, int label_id) {

		int content_id = contentidForTab(tag);
		
		View indicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.tab_label, mTabs, false);
		((TextView) indicator.findViewById(R.id.labelTextView))
				.setText(label_id);

		TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(content_id);

		return tabSpec;
	}

	private void updateTab(String tabTag) {
		int placeholder_id = placeholderIdForTab(tabTag);

		FragmentManager fm = getFragmentManager();
		if (fm.findFragmentByTag(tabTag) == null) {

			Fragment frag = fragmentForTab(tabTag);

			if (frag != null) {
				fm.beginTransaction().replace(placeholder_id, frag, tabTag)
						.commit();
			}
		}
	}
	
	private String tagForTabId(int tabId) {
		if (TAB_CONTACTS_ID == tabId) {
			return TAB_CONTACTS_TAG;
		} else if (TAB_GROUPS_ID == tabId) {
			return TAB_GROUPS_TAG;
		}
		return null;
	}

	private int placeholderIdForTab(String tabTag) {
		if (TAB_CONTACTS_TAG.equals(tabTag)) {
			return TAB_CONTACTS_ID;
		} else if (TAB_GROUPS_TAG.equals(tabTag)) {
			return TAB_GROUPS_ID;
		}
		return -1;
	}

	private Fragment fragmentForTab(String tabTag) {
		if (TAB_CONTACTS_TAG.equals(tabTag)) {
			return mProvideListContactsFragment.get();
		} else if (TAB_GROUPS_TAG.equals(tabTag)) {
			return mProvideListGroupsFragment.get();
		}
		return null;
	}
	
	private int contentidForTab(String tabTag) {
		if (TAB_CONTACTS_TAG.equals(tabTag)) {
			return TAB_CONTACTS_CONTENT;
		} else if (TAB_GROUPS_TAG.equals(tabTag)) {
			return TAB_GROUPS_CONTENT;
		}
		return -1;
	}

}
