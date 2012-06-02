package com.danielstiner.vibrates.views;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.utility.PatternEditManager;
import com.google.inject.Inject;

public class VibratePatternEdit extends RoboActivity {

	private static final String NS = com.danielstiner.vibrates.Vibrates.NS
			+ "." + "views";
	private static final String CLASSNAME = NS + "." + "VibratePatternEdit";

	public static final String EXTRA_KEY_PATTERN = CLASSNAME + "." + "pattern";

	private static final int CONTENT_VIEW = R.layout.pattern_edit;
	protected static final long[] PATTERN_EMPTY = new long[] { 0, 0 };

	@InjectView(R.id.pattern_edit_patternview)
	private VibratePatternView pattern_view;

	@InjectView(R.id.pattern_edit_clear_button)
	private Button pattern_clear;

	@Inject
	private PatternEditManager _editManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(CONTENT_VIEW);

		// Save the current pattern
		_editManager.setPattern(getVibratePattern(savedInstanceState));

		// Preview the current pattern
		_editManager.playPattern();
		pattern_view.setPattern(_editManager.getPattern());

		// Handle when the pattern changes
		_editManager.setWatcher(new Runnable() {

			@Override
			public void run() {
				pattern_view.setPattern(_editManager.getPattern());
				endEdit();
			}

		});

		// Handle clear button
		pattern_clear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_editManager.setPattern(PATTERN_EMPTY);
				pattern_view.setPattern(_editManager.getPattern());
				endEdit();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(EXTRA_KEY_PATTERN, _editManager.getPattern());
	}

	@Override
	public boolean onTouchEvent(MotionEvent motion) {
		if (motion.getAction() == MotionEvent.ACTION_DOWN) {
			_editManager.press();
		} else if (motion.getAction() == MotionEvent.ACTION_UP) {
			_editManager.release();
		}

		pattern_view.setPattern(_editManager.getPattern());

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
		if (savedState != null) {
			// It was stored as Long[]
			long[] patterntmp = (long[]) savedState
					.getSerializable(EXTRA_KEY_PATTERN);
			for (int i = 0; i < patterntmp.length; i++)
				pattern.add(patterntmp[i]);
		}
		// Else try check passed intent for a pattern
		if (pattern.size() == 0) {
			// Pull in pattern from passed intent if possible
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				long[] patterntmp = extras.getLongArray(EXTRA_KEY_PATTERN);
				for (int i = 0; i < patterntmp.length; i++)
					pattern.add(patterntmp[i]);
			}
		}

		return pattern;
	}
}
