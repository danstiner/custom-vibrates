package com.danielstiner.vibrates.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.utility.PatternEditManager;
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

	private List<Long> _pattern;
	
	private boolean _editing;
	
	private long _last_edit_up;

	private Handler _editHandler;

	private Runnable _editWatcher;
	
	@Inject private PatternEditManager _editManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(CONTENT_VIEW);

		// Save the current pattern
		_editManager.setPattern(getVibratePattern(savedInstanceState));

		// Preview the current pattern
		_editManager.playPattern();
		
		// Handle when the pattern changes
		_editManager.setWatcher(new Runnable() {

			@Override
			public void run() {
				endEdit();
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
		//_editManager.cancel();
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
			_editManager.press();
		}
		else if(motion.getAction() == MotionEvent.ACTION_UP)
		{
			_editManager.release();
		}
		
		return true;
	}
	
	private void endEdit() {
		// Save result for the calling activity to handle
		Intent i = new Intent();
		i.putExtra(EXTRA_KEY_PATTERN, _editManager.getPattern());
		setResult(Activity.RESULT_OK, i);
		
		// Play back
		_editManager.playPattern();
	}

	private void saveState() {
		
	}

	private List<Long> getVibratePattern(Bundle savedState) {
		List<Long> pattern = new ArrayList<Long>();
		
		// Recover saved state pattern if possible
		if(savedState != null && _pattern.size() == 0) {
			// It was stored as Long[]
			Long[] patterntmp = (Long[]) savedState.getSerializable(EXTRA_KEY_PATTERN);
			for(int i=0; i<patterntmp.length; i++)
				pattern.add(patterntmp[i]);
		}
		// Else try check passed intent for a pattern
		if (pattern.size() == 0) {
			// Pull in pattern from passed intent if possible
			Bundle extras = getIntent().getExtras();
			if(extras != null) {
				long[] patterntmp = extras.getLongArray(EXTRA_KEY_PATTERN);
				for(int i=0; i<patterntmp.length; i++)
					pattern.add(patterntmp[i]);
			}
		}
		
		return pattern;
	}
}
