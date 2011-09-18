package com.danielstiner.vibrates.views;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.database.IManager;
import com.danielstiner.vibrates.settings.IUserSettings;
import com.danielstiner.vibrates.utility.PatternEditManager;
import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ManualSetup extends RoboActivity {

	
	private static final int ACTIVITY_ADD_CONTACT_CHOOSE = 1;

	@InjectView(R.id.setup_manual_openaccessiblity) private Button buttonOpenAccessibility;
	
	@InjectView(R.id.setup_manual_defaultpattern_editbox) private LinearLayout defaultPatternEditBox;
	
	@InjectView(R.id.setup_manual_defaultpattern) private VibratePatternView defaultPatternView;
	
	@InjectView(R.id.setup_manual_addcontact) private Button buttonAddContact;
	
	@InjectView(R.id.setup_manual_complete) private Button buttonComplete;
	
	@Inject private IManager manager;
	
	@Inject private PatternEditManager defaultPatternEditManager;
	
	@Inject private IUserSettings user_settings;
	
	private Intent startMainScreenIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startMainScreenIntent = new Intent(this, EntityList.class);
		
		setTitle("Custom Vibrates: Quick Setup");
		
		setContentView(R.layout.setup_manual);
		
		setupView();
		setupDefaultPatternEdit();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case ACTIVITY_ADD_CONTACT_CHOOSE:
			if (resultCode == RESULT_OK && data != null) {
				Uri contactpath = data.getData();	
				manager.createFromContactUri(contactpath);
			}
			break;
		}
	}
	
	private void setupDefaultPatternEdit()
	{
		defaultPatternEditManager.setPattern(user_settings.defaultPattern());
		
		updateDefaultPatternView();
		
		// Handle when the pattern changes
		defaultPatternEditManager.setWatcher(new Runnable() {
			@Override
			public void run() {
				// Play it off
				defaultPatternEditManager.playPattern();
				
				// and save it
				user_settings.defaultPattern(defaultPatternEditManager.getPattern());
				
				// and display it
				updateDefaultPatternView();
			}
		});
		// Implementation of touches to pattern editing
		defaultPatternEditBox.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent motion) {
				
				// handle it
				if(motion.getAction() == MotionEvent.ACTION_DOWN)
				{
					defaultPatternEditManager.press();
				}
				else if(motion.getAction() == MotionEvent.ACTION_UP)
				{
					defaultPatternEditManager.release();
				}
				
				// and display what was handled
				updateDefaultPatternView();
				
				// We have taken care of this touch event
				return true;
			}
		});
		
	}
	private void updateDefaultPatternView() {
		defaultPatternView.setPattern(defaultPatternEditManager.getPattern());
	}
	
	private void setupView() {
		
		buttonOpenAccessibility.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
			}
		});
		buttonAddContact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(contactPickerIntent, ACTIVITY_ADD_CONTACT_CHOOSE);
			}
		});
		buttonComplete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Enable ourselves!
				user_settings.enabled(true);
				
				startActivity(startMainScreenIntent);
			}
		});
		
	}
	
}
