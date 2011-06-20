package com.danielstiner.vibrates;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class VibratePatternEdit extends Activity implements OnTouchListener {

	private static final int STATE_EDITING_DOWN = 1;

	private static final int STATE_WAITING = 0;

	private static final int STATE_EDITING_UP = 2;

	private static final long EDITING_WATCHER_DELAY = 0;

	private static final long EDITING_MAX_UP = 4*1000;

	private Vibrator _vibratr;

	private long[] HOLD_PATTERN = { 0, Long.MAX_VALUE };

	private List<Long> _pattern;

	private int _edit_state;

	private long _last_edit_up;

	private Handler _editHandler;

	private Runnable _editWatcher;
	
	public VibratePatternEdit() {
		super();
		
		// Initialize variables
		_pattern = new LinkedList<Long>();
		_editHandler = new Handler();
		_editWatcher = new Runnable() {

			@Override
			public void run() {
				editWatcherTick();
			}
			
		};
		_edit_state = STATE_WAITING;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup a vibrator hook to give live feedback
		_vibratr = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		// Get the current pattern
		getVibratePattern(savedInstanceState);

		// Check if we should preview the contact's current
		previewPatternIfNeeded(savedInstanceState);
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(CustomContactManager.KEY_VIBRATE_PATTERN, _pattern.toArray());
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
		// pass off work
		onTouch(null, motion);
		
		return true;
	}

	@Override
	public boolean onTouch(View view, MotionEvent motion) {
		// Start vibration
		if(motion.getAction() == MotionEvent.ACTION_DOWN) {
			// Vibrate till the user lets up
			_vibratr.vibrate(Long.MAX_VALUE);
			
			// Update state to reflect us editing the pattern
			if(_edit_state == STATE_WAITING) {
				
				// Clear any previous pattern
				_pattern.clear();
				// Init pattern with zero wait before first vibrate
				_pattern.add((long)0);
				// Set our edit state
				_edit_state = STATE_EDITING_DOWN;
				// Start 
				
			} else if(_edit_state == STATE_EDITING_UP) {
				// Going back down for another vibrate block
				// change the state
				_edit_state = STATE_EDITING_DOWN;
				// and store how long we were up
				_pattern.add(motion.getEventTime() - _last_edit_up);
			} else {
				// Something went wrong...
				// TODO throw new System();
			}
		} else if(motion.getAction() == MotionEvent.ACTION_UP) {
			// Check what edit state we are int
			if(_edit_state == STATE_EDITING_DOWN) {
				// Store this vibrate length
				_pattern.add(motion.getDownTime());
				// Update state to reflect end of gesture
				_edit_state = STATE_EDITING_UP;
				// cache the time when it happened
				_last_edit_up = motion.getEventTime();
			} else {
				// Something went wrong...
				// TODO throw new Exception();
			}
		}
		return false;
	}
	
	protected void editWatcherTick() {
		// Call ourselves again in a bit
		_editHandler.postDelayed(_editWatcher, EDITING_WATCHER_DELAY);
		
		// See if it has been a long time since the user let up their finger
		// if so, play back their pattern
		if(_edit_state == STATE_EDITING_UP && System.currentTimeMillis() - _last_edit_up > EDITING_MAX_UP)
			endEdit(true);
	}
	
	private void playPattern() {
		// Make a copy of the pattern for playing
		long[] pattern = new long[_pattern.size()];
		for(int i=0; i<_pattern.size(); i++)
			pattern[i] = _pattern.get(i).longValue();
		// Play it off
		_vibratr.vibrate(pattern, -1);
	}
	
	private void endEdit() {
		// Do not playback the pattern by default
		endEdit(false);
	}
	private void endEdit(boolean playback_pattern) {
		// Reset state if needed
		if(_edit_state == STATE_EDITING_UP || _edit_state == STATE_EDITING_DOWN) {
			
			// Don't need to add anything else to the pattern, no finger was down
			// or maybe it was, but something else crazy happened, so lets ignore it
			
			// Clean up any running callbacks
			_editHandler.removeCallbacks(_editWatcher);
			
			// Change state back to default
			_edit_state = STATE_WAITING;
		}
		
		// Stop any ongoing vibrations
		if (_vibratr != null) {
			_vibratr.cancel();
		}
		
		// Play back if needed
		if(playback_pattern == true)
			playPattern();
	}

	private void saveState() {
	}

	private void previewPatternIfNeeded(Bundle savedState) {
		// TODO
		playPattern();
	}

	private void getVibratePattern(Bundle savedState) {
		// Recover saved state pattern if possible
		if(savedState != null && _pattern.size() == 0) {
			// It was stored as Long[]
			Long[] pattern = (Long[]) savedState.getSerializable(CustomContactManager.KEY_VIBRATE_PATTERN);
			for(int i=0; i<pattern.length; i++)
				_pattern.add(pattern[i]);
		}
		// Else try check passed intent for a pattern
		if (_pattern.size() == 0) {
			// Pull in pattern from passed intent if possible
			Bundle extras = getIntent().getExtras();
			if(extras != null) {
				long[] pattern = extras.getLongArray(CustomContactManager.KEY_VIBRATE_PATTERN);
				for(int i=0; i<pattern.length; i++)
					_pattern.add(pattern[i]);
			}
		}
	}
}
