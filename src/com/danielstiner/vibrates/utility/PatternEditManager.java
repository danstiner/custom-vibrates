package com.danielstiner.vibrates.utility;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import android.os.Handler;
import android.os.Vibrator;

public class PatternEditManager {
	private static final long EDITING_WATCHER_DELAY = 10;
	private static final long EDITING_MAX_UP = 1500;
	// One whole day, just to be sure
	private static final long EDITING_MAX_DOWN = 24 * 60 * 60 * 1000;
	
	private Vibrator _vibratr;

	private List<Long> _pattern;
	
	private boolean _editing;
	
	private long _last_edit_up;
	private long _last_edit_down;

	private Handler _editHandler;

	private Runnable _editWatcher;
	
	private Runnable _newPattern;
	
	@Inject
	public PatternEditManager(Vibrator vibratr) {
		this._vibratr = vibratr;
		
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
	}
	
	public void setWatcher(Runnable onCompleteNewPattern)
	{
		_newPattern = onCompleteNewPattern;
	}
	
	public void setPattern(List<Long> newPattern) {
		_pattern = newPattern;
		
		// Tell out listener if there is one that a new pattern exists
		if(_newPattern != null)
		{
			_newPattern.run();
		}
	}
	
	public void setPattern(long[] pattern) {
		List<Long> pattern_list = new ArrayList<Long>(pattern.length);
		
		for(int i=0; i<pattern.length; i++) {
			pattern_list.add(pattern[i]);
		}
		
		setPattern(pattern_list);
	}
	
	public long[] getPattern() {
		return PatternEditManager.asLongArray(_pattern);
	}
	
	public void playPattern() {
		_vibratr.vibrate(this.getPattern(), -1);
	}
	
	public void press() {
		
		_vibratr.vibrate(EDITING_MAX_DOWN);
		
		if(!_editing)
			startEdit();
		else
			editDown();
	}
	
	public void release() {
		
		_vibratr.cancel();
		editUp();
		
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
		_last_edit_down = System.currentTimeMillis();
		
		// Start timeout watcher
		_editHandler.postDelayed(_editWatcher, EDITING_WATCHER_DELAY);
	}
	
	private void editUp()
	{
		_pattern.add(System.currentTimeMillis() - _last_edit_down);
		_last_edit_up = System.currentTimeMillis();
		_last_edit_down = 0;
	}
	
	private void editDown()
	{
		_pattern.add(System.currentTimeMillis() - _last_edit_up);
		_last_edit_up = 0;
		_last_edit_down = System.currentTimeMillis();
	}
	
	private void endEdit() {
		
		// Stop any ongoing vibrations
		_vibratr.cancel();
		
		// Clean up any running callbacks and reset state
		_editHandler.removeCallbacks(_editWatcher);
		_editing = false;
		
		// Tell out listener if there is one that a new pattern exists
		if(_newPattern != null)
		{
			_newPattern.run();
		}
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
	
	private static long[] asLongArray(List<Long> list)
	{
		long[] array = new long[list.size()];
		
		for(int i=0; i<list.size(); i++)
			array[i] = list.get(i).longValue();
		
		return array;
	}
}
