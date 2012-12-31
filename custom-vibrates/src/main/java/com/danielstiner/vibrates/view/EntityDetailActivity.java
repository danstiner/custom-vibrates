package com.danielstiner.vibrates.view;

import roboguice.inject.ContextScopedProvider;
import roboguice.util.Ln;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.view.fragments.EntityDetailFragment;
import com.danielstiner.vibrates.view.model.OnMenuSettingsClickListener;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.inject.Inject;

public class EntityDetailActivity extends RoboSherlockFragmentActivity {

	public static void show(Entity e, Context context) {
		Intent i = new Intent(context, EntityDetailActivity.class);
		Entity.toIntent(i, e);
		context.startActivity(i);
	}

	@Inject
	private Application mAppContext;

	@Inject
	private ContextScopedProvider<IDataModel> mModelProvider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entity_detail);

		// Extract entity from incoming intent
		final Entity entity = extractEntity(getIntent());

		if (entity == null) {
			setTitle("Entity Detail Failure");
			Ln.e("Tried to start entity detail view without giving an entity identifier");
		} else {
			setTitle(entity.getKind().toDisplayString() + " Detail");

			((EntityDetailFragment) getSupportFragmentManager()
					.findFragmentById(R.id.entity_fragment))
					.onEntitySelected(entity);
		}

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			EntityDetailActivity.this.finish();
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
						OnMenuSettingsClickListener.getCompatInstance(this))
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return super.onCreateOptionsMenu(menu);
	}
}
