package com.danielstiner.vibrates.utility;

import roboguice.util.Ln;
import roboguice.util.RoboAsyncTask;

import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IIdentifierManager;
import com.google.inject.Inject;

public class SyncEntities extends RoboAsyncTask<Boolean> {

	private IEntityManager entity_manager;
	private IIdentifierManager identifier_manager;

	@Inject
	public SyncEntities(IEntityManager entity_manager,
			IIdentifierManager identifier_manager) {
		this.entity_manager = entity_manager;
		this.identifier_manager = identifier_manager;
	}

	@Override
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPreExecute() {
		// do this in the UI thread before executing call()

		// Grab all contacts
	}

	@Override
	protected void onException(Exception e) {
		// do this in the UI thread if call() threw an exception
		Ln.d(e, "Updating entities to be synced with external contacts failed.");
		// Toast.makeText(SearchActivity.this, failureMessage,
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onFinally() {
		// always do this in the UI thread after calling call()
		// if (dialog.isShowing())
		// dialog.dismiss();
	}

}
