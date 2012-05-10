package com.danielstiner.vibrates.view;

import roboguice.inject.ContextScopedProvider;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract.QuickContact;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.view.fragments.PatternEditFragment;
import com.danielstiner.vibrates.view.model.OnMenuSettingsClickListener;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntityDetail extends RoboSherlockFragmentActivity implements
		PatternEditFragment.ContainerActivityInterface {

	public static void show(Entity e, Context context) {
		Intent i = new Intent(context, EntityDetail.class);
		Entity.toIntent(i, e);
		context.startActivity(i);
	}

	private PatternEditFragment mPatternFragment;

	@Inject
	private Provider<PatternEditFragment> mPatternFragmentProvider;

	@Inject
	private Application mAppContext;

	@Inject
	private ContextScopedProvider<IDataModel> mModelProvider;
	
	@InjectView(R.id.contact_badge)
	private QuickContactBadge mQuickContactBadge;
	
	@InjectView(R.id.name)
	private TextView mNameTextView;
	
	@InjectView(R.id.type)
	private TextView mTypeTextView;

	private Entity mEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Move into tabs if we get more than one
		setContentView(R.layout.entity_detail);

		// Extract entity from incoming intent
		mEntity = extractEntity(getIntent());

		if (mEntity == null)
			setTitle("Entity Detail");
			// TODO Big failure, exit or something
		else
		{
			mNameTextView.setText(mEntity.getName());
			setTitle(mEntity.getKind().toDisplayString() + " Detail");
			mTypeTextView.setText(mEntity.getKind().toDisplayString());
		}

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.addTab(actionBar.newTab().setText("Pattern"));

		FragmentTransaction transact = getSupportFragmentManager()
				.beginTransaction();

		Fragment f = getSupportFragmentManager().findFragmentById(
				R.id.fragment_container);

		if (mPatternFragment == null) {
			mPatternFragment = mPatternFragmentProvider.get();

			transact.replace(R.id.fragment_container, mPatternFragment);

			transact.commit();
			
			if (mEntity != null)
				mPatternFragment.setPattern(mEntity.getPattern(), savedInstanceState==null);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			EntityDetail.this.finish();
		}

		return super.onOptionsItemSelected(item);
	}

	private Entity extractEntity(Intent intent) {
		IDataModel model = mModelProvider.get(mAppContext);
		
		return Entity.fromBundle(intent.getExtras(), model);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, Menu.NONE, Menu.FIRST, "Delete")
				.setIcon(R.drawable.content_discard)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(Menu.NONE, Menu.NONE, Menu.CATEGORY_CONTAINER, "Settings")
				.setIcon(R.drawable.action_settings)
				.setOnMenuItemClickListener(
						OnMenuSettingsClickListener.getInstance(this))
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onPatternChanged(Pattern p, PatternEditFragment in) {

		IDataModel model = mModelProvider.get(mAppContext);
		
		if (mEntity == null) {
			Ln.e("EntityDetail had no entity to change the pattern of");
			return;
		}
		
		mEntity.setPattern(p);
		
		model.update(mEntity);

	}
}
