package com.danielstiner.vibrates.view;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContextScopedProvider;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Entity.Kind;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.model.StorageUtil;
import com.google.inject.Inject;

public class EntityAddActivity extends RoboFragmentActivity {

	private static final String ENTITY_KIND_KEY = Entity.EXTRA_KEY_ID + ".kind";
	private static final int ACTIVITY_RESULT_CHOOSE_CONTACT = 0;
	private static final int RESULT_NO_ADD = 2;

	public static void show(Entity.Kind kind, Activity forActivity,
			int requestCode) {
		Intent i = new Intent((Context) forActivity, EntityAddActivity.class);
		i.putExtra(ENTITY_KIND_KEY, kind.toString());
		forActivity.startActivityForResult(i, requestCode);
	}

	@Inject
	private ContextScopedProvider<IDataModel> mModelProvider;

	@Inject
	private Application mAppContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Do different things based on the kind of entity
		// we are supposed to add
		if (getIntent().hasExtra(ENTITY_KIND_KEY)) {
			handleShow(Entity.Kind.fromString(getIntent().getStringExtra(
					ENTITY_KIND_KEY)));
		} else {
			// Default case
			// for now, just finish with an error code
			setResult(RESULT_NO_ADD);
			finish();
		}
	}

	private void handleShow(Kind entityKind) {
		if (entityKind.equals(Entity.Kind.Contact)) {
			Intent i = new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI);
			startActivityForResult(i, ACTIVITY_RESULT_CHOOSE_CONTACT);
		} else {
			// Default case
			// for now, just finish with an error code
			setResult(RESULT_NO_ADD);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case ACTIVITY_RESULT_CHOOSE_CONTACT:
			if (resultCode == RESULT_OK && data != null) {
				Uri contact_path = data.getData();

				IDataModel model = mModelProvider.get(mAppContext);

				StorageUtil.createContactFromUri(model, contact_path);

				this.setResult(RESULT_OK);
				this.finish();
			} else {
				this.setResult(RESULT_NO_ADD);
			}
		}

		this.setResult(RESULT_OK);
		this.finish();
	}

}
