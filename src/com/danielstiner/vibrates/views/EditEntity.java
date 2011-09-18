package com.danielstiner.vibrates.views;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IManager;
import com.danielstiner.vibrates.utility.PatternEditManager;
import com.google.inject.Inject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditEntity extends RoboActivity {

	private static final int ACTIVITY_PATTERN_EDIT = 1;
	
	@Inject private IManager manager;
	
	@Inject private IEntityManager entity_manager;
	
	@Inject private PatternEditManager _editManager;
	
	@InjectView(R.id.entity_edit_image) private ImageView entity_image;
	
	@InjectView(R.id.entity_edit_name) private TextView entity_name;
	
	@InjectView(R.id.entity_edit_type) private TextView entity_type;
	
	@InjectView(R.id.entity_edit_pattern) private VibratePatternView entity_pattern_view;
	
	@InjectView(R.id.entity_edit_remove) private Button delete_button;
	
	@InjectView(R.id.entity_edit_linearlayout_details) private LinearLayout entity_details;
	
	@InjectView(R.id.entity_edit_linearlayout_patternedit) private LinearLayout entity_pattern_edit;
	
	private static final int CONTENT_VIEW = R.layout.entity_edit;

	private Entity _entity;

	private long mContactId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(CONTENT_VIEW);
		setTitle(R.string.entityedit_title);
		
		// Build a contact to represent who we are customizing
		_entity = getEntity(savedInstanceState);
		// and update it
		// TODO: Updates should be batched in the list view, this should not be needed then
		manager.update(_entity);
		
		// Pattern editing stuff
		_editManager.setPattern(manager.getPattern(_entity, null));
		_editManager.playPattern();
		// Handle when the pattern changes
		_editManager.setWatcher(new Runnable() {
			@Override
			public void run() {
				// Play it off
				_editManager.playPattern();
				
				// and save it
				manager.setPattern(_entity, _editManager.getPattern());
				
				// and display it
				updatePatternView();
			}
		});
		// Implementation of touches to pattern editing
		entity_pattern_edit.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent motion) {
				
				// handle it
				if(motion.getAction() == MotionEvent.ACTION_DOWN)
				{
					_editManager.press();
				}
				else if(motion.getAction() == MotionEvent.ACTION_UP)
				{
					_editManager.release();
				}
				
				// and display what was handled
				updatePatternView();
				
				// We have taken care of this touch event
				return true;
			}
		});
		
		// Time for some real work
		populateFields();
		attachClickListeners();
		updatePatternView();
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		switch (requestCode) {
//		case ACTIVITY_PATTERN_EDIT:
//			// Refresh contact list
//			if (resultCode == RESULT_OK && data != null)
//			{
//				manager.setPattern(_entity, data.getLongArrayExtra(VibratePatternEdit.EXTRA_KEY_PATTERN));
//			}
//			break;
//		}
//	}
	
//	private void editPattern(String type) {
//		Intent i = new Intent(this, VibratePatternEdit.class);
//		i.putExtra(VibratePatternEdit.EXTRA_KEY_PATTERN, manager.getPattern(_entity, null));
//		startActivityForResult(i, ACTIVITY_PATTERN_EDIT);
//	}
	
	 @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(Entity.EXTRA_KEY_ID, _entity.entityid());
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
        
        // Stop any ongoing vibrations
        ((Vibrator)this.getSystemService(VIBRATOR_SERVICE)).cancel();
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
		
		// Title field
		//setTitle("Customize: " + manager.getDisplayName(_entity));
		
		// Name
		if(entity_name != null)
			entity_name.setText(manager.getDisplayName(_entity));
		
		// Type
		if(entity_type != null)
		{
			// TODO Do this prettyfing somewhere else
			String kind = manager.getKind(_entity);
			if(kind.equals(Entity.TYPE_CONTACT))
				entity_type.setText("Contact");
			else
				entity_type.setText("");
		}
		
		// Image
		if (entity_image != null) {
			entity_image.setImageDrawable(Drawable.createFromStream(manager.getPhotoStream(_entity), "contactphoto"));
		}
		
		
	}
	
	private void updatePatternView()
	{
		if(entity_pattern_view != null)
			entity_pattern_view.setPattern(_editManager.getPattern());
	}
	
	private void attachClickListeners() {
		// View contact
		entity_details.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				viewEntityDetails();
			}
		});
		
		// Remove customizations
		delete_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// view entity details
				removeEntity();
			}
		});
		
	}
	private void viewEntityDetails()
	{
		// view contact details if possible, otherwise do nothing
		Intent viewEntity = manager.getViewIntent(_entity);
		if(viewEntity != null)
		{
			startActivity(viewEntity);
		}
	}
	private void removeEntity()
	{
		manager.remove(_entity);
		
		this.finish();
	}
	
	private Entity getEntity(Bundle savedInstanceState) {
		if(savedInstanceState != null) {
			mContactId = (Long) savedInstanceState.getSerializable(Entity.EXTRA_KEY_ID);
		} else if(getIntent() != null && getIntent().getExtras() != null) {
            mContactId = getIntent().getExtras().getLong(Entity.EXTRA_KEY_ID);
		} else {
			// TODO, bad
			//throw new Exception("Didn't get a row to edit.");
			Ln.d("No entity id given to edit, canceling activity");
			Intent mIntent = new Intent();
	    	setResult(RESULT_CANCELED, mIntent);
	    	finish();
		}
		
		// Actually create contact
		return entity_manager.get(mContactId);
	}
}
