package com.danielstiner.vibrates.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.view.fragments.EditEntity;
import com.danielstiner.vibrates.view.fragments.ListEntities;
import com.google.inject.Inject;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectFragment;

public class Entities extends RoboFragmentActivity {

	@Inject
	private ListEntities mEntityListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.entities);

		FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
		// TODO
		fm.add(mEntityListFragment, arg1);
	}

	private void onEntitySelected(Entity e) {
		EditEntity editEntity = (EditEntity) getSupportFragmentManager()
				.findFragmentById(R.id.entity_edit_fragment);

		if (editEntity == null || !editEntity.isInLayout()) {
			EntityDetail.show(e, getApplicationContext());
		}
	}
}
