package com.danielstiner.vibrates.views;

import java.util.LinkedList;
import java.util.List;

import com.danielstiner.vibrates.R;
import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class VibratePatternEdit extends RoboActivity { //implements OnTouchListener {

	private static final String NS = com.danielstiner.vibrates.Vibrates.NS + "." + "views";
	private static final String CLASSNAME = NS + "." + "VibratePatternEdit";
	
	public static final String PATTERN_BUNDLE_KEY = CLASSNAME + "." + "pattern";

	private static final long EDITING_WATCHER_DELAY = 10;
	private static final long EDITING_MAX_UP = 2 * 1000;
	// One day, just to be sure
	private static final long EDITING_MAX_DOWN = 24 * 60 * 60 * 1000;
	
	private static final int CONTENT_VIEW = R.layout.pattern_edit;

	@InjectView(R.id.pattern_edit_patternview) private VibratePatternView pattern_view;
	
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
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(PATTERN_BUNDLE_KEY, _pattern.toArray());
	}

	@Override
	protected void onPause() {
		super.onPause();
		endEdit();
		saveState();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		endEdit();
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
			Long[] pattern = (Long[]) savedState.getSerializable(PATTERN_BUNDLE_KEY);
			for(int i=0; i<pattern.length; i++)
				_pattern.add(pattern[i]);
		}
		// Else try check passed intent for a pattern
		if (_pattern.size() == 0) {
			// Pull in pattern from passed intent if possible
			Bundle extras = getIntent().getExtras();
			if(extras != null) {
				long[] pattern = extras.getLongArray(PATTERN_BUNDLE_KEY);
				for(int i=0; i<pattern.length; i++)
					_pattern.add(pattern[i]);
			}
		}
	}
	
	private void playPattern() {
		// Make a copy of the pattern for playing
		long[] pattern = new long[_pattern.size()];
		for(int i=0; i<_pattern.size(); i++)
			pattern[i] = _pattern.get(i).longValue();
		// Play it off
		_vibratr.vibrate(pattern, -1);
	}
}
