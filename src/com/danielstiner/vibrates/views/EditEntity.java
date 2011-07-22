package com.danielstiner.vibrates.views;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.R.id;
import com.danielstiner.vibrates.R.layout;
import com.danielstiner.vibrates.R.string;
import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IIdentifierManager;
import com.danielstiner.vibrates.database.IPatternManager;
import com.google.inject.Inject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditEntity extends Activity {

	private static final int ACTIVITY_PATTERN_EDIT = 1;
	
	@Inject IEntityManager entity_manager;
	
	@Inject IPatternManager pattern_manager;
	
	@Inject IIdentifierManager identifier_manager;

	private int mContactRowId;

	private String mLookupKey;
	
	private Entity _entity;

	private String mContactLookup;

	private long mContactId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contactedit);
		setTitle(R.string.contactedit_title);
		
		// Build a contact to represent who we are customizing
		_entity = getEntity(savedInstanceState);
		// and update it
		entity_manager.update(_entity);
		// and its identifiers
		identifier_manager.update(_entity);
		
		// Time for some real work
		playDefaultPattern();
		populateFields();
		
		Button editDefaultButton = (Button) findViewById(R.id.edit_default_button);
		//Button deleteButton = (Button) findViewById(R.id.edit_delete_button);
		Button saveButton = (Button) findViewById(R.id.edit_save_button);
		
		saveButton.setOnClickListener(new View.OnClickListener() {

		    public void onClick(View view) {
		    	setResult(RESULT_OK, new Intent());
		    	finish();
		    }

		});
		
		editDefaultButton.setOnClickListener(new View.OnClickListener() {

		    public void onClick(View view) {
		    	// Start pattern edit
				editPattern(null);
		    }

		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Bundle extras = data.getExtras();

		switch (requestCode) {
		}
	}
	
	private void editPattern(String type) {
		Intent i = new Intent(this, VibratePatternEdit.class);
		i.putExtra(VibratePatternEdit.PATTERN_BUNDLE_KEY, pattern_manager.get(_entity, type));
		startActivityForResult(i, ACTIVITY_PATTERN_EDIT);
	}
	
	 @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(Entity.ID_BUNDLE_KEY, _entity.entityid());
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    private void saveState() {
        //String title = mTitleText.getText().toString();
        //String body = mBodyText.getText().toString();
/*
        if (mRowId == null) {
            //long id = mDbHelper.createNote(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, body);
	    }*/
    }
	
	private void populateFields() {
		TextView contact_name = (TextView) findViewById(R.id.edit_name);
		if(contact_name != null)
			contact_name.setText(entity_manager.getDisplayName(_entity));
		
		VibratePatternView pattern;
		
		pattern = (VibratePatternView) findViewById(R.id.edit_default_pattern);
		if(pattern != null)
			pattern.setPattern(pattern_manager.get(_entity));
	}
	
	private Entity getEntity(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			mContactId = (Long) savedInstanceState.getSerializable(Entity.ID_BUNDLE_KEY);
		} else if(getIntent().getExtras() != null) {
            mContactId = getIntent().getExtras().getLong(Entity.ID_BUNDLE_KEY);
		} else {
			// TODO, bad
			//throw new Exception("Didn't get a row to edit.");
			Intent mIntent = new Intent();
	    	setResult(RESULT_CANCELED, mIntent);
	    	finish();
		}
		
		// Actually create contact
		return entity_manager.get(mContactId);
	}
	private void playDefaultPattern() {
		// Play it off
		((Vibrator)this.getSystemService(VIBRATOR_SERVICE)).vibrate(pattern_manager.get(_entity), -1);
		/*
		 * // Play the appropriate vibrate pattern
		Intent service = new Intent(context, VibratrService.class);
		service.putExtra(CustomContactManager.KEY_VIBRATE_IDENTIFIER, msg
				.getDisplayOriginatingAddress());
		service.putExtra(CustomContactManager.KEY_VIBRATE_MIMETYPE,
				CustomContactManager.MIMETYPE_DEFAULT);
		context.startService(service);
		VibratrService
		 */
	}
}
