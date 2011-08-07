package com.danielstiner.vibrates.views;

import java.util.LinkedList;
import java.util.List;

import com.danielstiner.vibrates.R;
import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class VibratePatternEdit extends RoboActivity {

	private static final String NS = com.danielstiner.vibrates.Vibrates.NS + "." + "views";
	private static final String CLASSNAME = NS + "." + "VibratePatternEdit";
	
	public static final String EXTRA_KEY_PATTERN = CLASSNAME + "." + "pattern";

	private static final long EDITING_WATCHER_DELAY = 10;
	private static final long EDITING_MAX_UP = 1500;
	// One whole day, just to be sure
	private static final long EDITING_MAX_DOWN = 24 * 60 * 60 * 1000;
	
	private static final int CONTENT_VIEW = R.layout.pattern_edit;

	@InjectView(R.id.pattern_edit_patternview) private VibratePatternView pattern_view;
	
	@InjectView(R.id.pattern_edit_finish_button) private Button finish_button;
	
	@Inject private Vibrator _vibratr;

	private List<Long> _pattern;
	
	private boolean _editing;
	
	private long _last_edit_up;

	private Handler _editHandler;

	private Runnable _editWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(CONTENT_VIEW);
		
		// Initialize variables
		_editing = false;
		_pattern = new LinkedList<Long>();
		_editHandler = new Handler();
		_editWatcher = new Runnable() {

			@Override
			public void run() {
				editWatcherTick();
			}
			
		};

		// Get the current pattern
		getVibratePattern(savedInstanceState);

		// Preview the current pattern
		playPattern();
		
		// Content stuff
		finish_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				endEdit();
				finish();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(EXTRA_KEY_PATTERN, _pattern.toArray());
	}

	@Override
	protected void onPause() {
		super.onPause();
		endEdit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//endEdit();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent motion) {
		if(motion.getAction() == MotionEvent.ACTION_DOWN)
		{
			_vibratr.vibrate(EDITING_MAX_DOWN);
			
			if(!_editing)
				startEdit();
			else
				editDown(motion);
		}
		else if(motion.getAction() == MotionEvent.ACTION_UP)
		{
			_vibratr.cancel();
			editUp(motion);
		}
		
		return true;
	}
	
	private void startEdit()
	{
		// Clear any previous pattern
		_pattern.clear();
		// Init pattern with zero wait before first vibrate
		_pattern.add((long)0);
		// Set our edit state
		_editing = true;
		_last_edit_up = 0;
		
		// Start timeout watcher
		_editHandler.postDelayed(_editWatcher, EDITING_WATCHER_DELAY);
	}
	
	private void editUp(MotionEvent motion)
	{
		_pattern.add(motion.getEventTime() - motion.getDownTime());
		_last_edit_up = System.currentTimeMillis();
		pattern_view.setPattern(_pattern);
	}
	
	private void editDown(MotionEvent motion)
	{
		_pattern.add(System.currentTimeMillis() - _last_edit_up);
		_last_edit_up = 0;
		pattern_view.setPattern(_pattern);
	}
	
	private void endEdit() {
		
		// Stop any ongoing vibrations
		_vibratr.cancel();
		
		// Clean up any running callbacks and reset state
		_editHandler.removeCallbacks(_editWatcher);
		_editing = false;
		
		// Save result for the calling activity to handle
		Intent i = new Intent();
		i.putExtra(EXTRA_KEY_PATTERN, asLongArray(_pattern));
		setResult(Activity.RESULT_OK, i);
		
		// Play back if needed
		// TODO if(playback_pattern == true)
		playPattern();
	}
	
	private void editWatcherTick() {
		// Call ourselves again in a bit
		_editHandler.postDelayed(_editWatcher, EDITING_WATCHER_DELAY);
		
		// See if it has been a long time since the user let up their finger
		// if so, play back their pattern
		long diff = System.currentTimeMillis() - _last_edit_up;
		if(_last_edit_up != 0 && diff > EDITING_MAX_UP)
			endEdit();
	}

	private void saveState() {
		
	}

	private void getVibratePattern(Bundle savedState) {
		// Recover saved state pattern if possible
		if(savedState != null && _pattern.size() == 0) {
			// It was stored as Long[]
			Long[] pattern = (Long[]) savedState.getSerializable(EXTRA_KEY_PATTERN);
			for(int i=0; i<pattern.length; i++)
				_pattern.add(pattern[i]);
		}
		// Else try check passed intent for a pattern
		if (_pattern.size() == 0) {
			// Pull in pattern from passed intent if possible
			Bundle extras = getIntent().getExtras();
			if(extras != null) {
				long[] pattern = extras.getLongArray(EXTRA_KEY_PATTERN);
				for(int i=0; i<pattern.length; i++)
					_pattern.add(pattern[i]);
			}
		}
	}
	
	private void playPattern() {
		// Make a copy of the pattern for playing
		
		// Play it off
		_vibratr.vibrate(asLongArray(_pattern), -1);
	}
	
	private long[] asLongArray(List<Long> list)
	{
		long[] array = new long[list.size()];
		
		for(int i=0; i<list.size(); i++)
			array[i] = list.get(i).longValue();
		
		return array;
	}
}
