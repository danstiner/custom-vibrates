package com.danielstiner.vibrates.view.fragments;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.util.PatternEditManager;
import com.danielstiner.vibrates.view.components.PatternView;
import com.google.inject.Inject;

public class PatternEditFragment extends RoboFragment implements
		IPatternEditFragment {

	public interface ContainerActivityInterface {
		public void onPatternChanged(Pattern p, PatternEditFragment in);
	}

	private static final String NS = com.danielstiner.vibrates.Vibrates.NS
			+ "." + "views";
	private static final String CLASSNAME = NS + "." + "VibratePatternEdit";
	private static final String EXTRA_KEY_PATTERN = CLASSNAME + "." + "pattern";

	@InjectView(R.id.patternview)
	private PatternView mPatternView;

	@InjectView(R.id.fragment_container)
	private LinearLayout mLayout;

	@Inject
	private PatternEditManager mEditManager;

	//private Pattern mPattern = Pattern.NONE;

	public PatternEditFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setPattern(getSavedPattern(savedInstanceState), false);

		mEditManager.setWatcher(new Runnable() {

			@Override
			public void run() {
				// Got new complete pattern
				onNewPattern();
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString(mEditManager.getPattern().toString(), EXTRA_KEY_PATTERN);
	}

	protected void onNewPattern() {

		if (mPatternView != null)
			mPatternView.setPattern(mEditManager.getPattern());

		// Preview the current pattern
		mEditManager.playPattern();

		// Try and talk to parent activity
		ContainerActivityInterface cai = getContainer();
		if (cai != null)
			cai.onPatternChanged(mEditManager.getPattern(), this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Force correct container type
		getContainer();
	}

	private ContainerActivityInterface getContainer() {

		Activity activity = getActivity();

		if (activity == null)
			return null;

		try {
			return (ContainerActivityInterface) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement ContainerActivityInterface");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null)
			return null;

		return inflater.inflate(R.layout.fragment_pattern_edit, container,
				false);
	}

	// Called after onCreateView
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Injects members through roboguice
		super.onViewCreated(view, savedInstanceState);

		if (mPatternView != null)
			mPatternView.setPattern(mEditManager.getPattern());

		// TODO attach touch handlers
		mLayout.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					PatternEditFragment.this.mEditManager.press();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					PatternEditFragment.this.mEditManager.release();
				}

				PatternEditFragment.this.mPatternView.setPattern(PatternEditFragment.this.mEditManager.getPattern());

				return true;
			}
		});
	}

	@Override
	public void setPattern(Pattern pattern, boolean play) {
		mEditManager.setPattern(pattern);

		if (mPatternView != null)
			mPatternView.setPattern(mEditManager.getPattern());

		// Preview the current pattern
		if (play)
			mEditManager.playPattern();
	}

	@Override
	public void onStop() {
		super.onStop();

		// stop any current vibrations
		mEditManager.stopPlay();
	}

	@Override
	public Pattern getPattern() {
		return mEditManager.getPattern();
	}

	private Pattern getSavedPattern(Bundle savedState) {

		if (savedState == null)
			return Pattern.NONE;
		else if (savedState.getString(EXTRA_KEY_PATTERN) == null)
			return Pattern.NONE;
		else
			return Pattern.fromString(savedState.getString(EXTRA_KEY_PATTERN));
	}

}
