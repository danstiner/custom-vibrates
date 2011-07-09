package com.danielstiner.vibrates;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

	private int mContactRowId;
	
	private Manager _ccm;

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
		
		// setup database for saving changes / getting additional details
		_ccm = new Manager(this);
		
		// Build a contact to represent who we are customizing
		initContact(savedInstanceState);
		
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
				editPattern(Notification.DEFAULT);
		    }

		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Bundle extras = data.getExtras();

		switch (requestCode) {/*
		case ACTIVITY_EDIT:
			//if(extras != null) {
			//Long contactId = extras.getLong(CustomContactManager.KEY_CONTACT_ID);

			// Refresh contact list if needed
			if (resultCode == RESULT_OK) {
				fillContactList();
			}

			//}
			break;
		case ACTIVITY_PICK_CONTACT:
			if (resultCode == RESULT_OK) {
				// Intent i = new Intent(this, ContactEdit.class);
				// startActivityForResult(i, ACTIVITY_CREATE);

				Set<String> keys = extras.keySet();
				Iterator<String> iterate = keys.iterator();
				while (iterate.hasNext()) {
					String key = iterate.next();
					Log.v(DEBUG_TAG, key + "[" + extras.get(key) + "]");
				}
				Uri contactpath = data.getData();
				// Example uri: content://com.android.contacts/contacts/lookup/0r7-2C46324E483C324A3A484634/7
				Log.v(DEBUG_TAG, "Got a result: " + contactpath.toString());
				
				// get the contact id from the Uri
				String id = contactpath.getLastPathSegment();
				
				// get the permanent lookup key in case the id changes
				List<String> resultSegments = contactpath.getPathSegments();
				// hint, its the second to last segment
				String lookup = resultSegments.get(resultSegments.size()-2);

				// Actually create contact in DB
				Contact newContact = ccm.getContact(Long.parseLong(id), lookup);
				ccm.add(newContact);
				editContact(newContact);

			} else {
				// gracefully handle failure
				Log.w(DEBUG_TAG, "Warning: activity result not ok");
			}
			break;*/
		}
	}
	
	private void editPattern(String type) {
		Intent i = new Intent(this, VibratePatternEdit.class);
		i.putExtra(Notification.PATTERN_KEY, _ccm.getPattern(_entity));
		startActivityForResult(i, ACTIVITY_PATTERN_EDIT);
	}
	
	 @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(Manager.ENTITY_ID_KEY, _entity.getId());
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
			contact_name.setText(_ccm.getDisplayName(_entity));
		
		VibratePatternView pattern;
		
		pattern = (VibratePatternView) findViewById(R.id.edit_default_pattern);
		if(pattern != null)
			pattern.setPattern(_ccm.getPattern(_entity));
	}
	
	private void initContact(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			mContactId = (Long) savedInstanceState.getSerializable(Manager.ENTITY_ID_KEY);
		} else if(getIntent().getExtras() != null) {
            mContactId = getIntent().getExtras().getLong(Manager.ENTITY_ID_KEY);
		} else {
			// TODO, bad
			//throw new Exception("Didn't get a row to edit.");
			Intent mIntent = new Intent();
	    	setResult(RESULT_CANCELED, mIntent);
	    	finish();
		}
		
		// Actually create contact
		_entity = _ccm.get(mContactId);
	}
	private void playDefaultPattern() {
		// Play it off
		((Vibrator)this.getSystemService(VIBRATOR_SERVICE)).vibrate(_ccm.getPattern(_entity), -1);
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
