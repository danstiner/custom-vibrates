package com.danielstiner.vibrates.settings;

import roboguice.activity.RoboActivity;
import android.content.Intent;

import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.view.fragments.PatternEdit;
import com.google.inject.Inject;

public class DefaultPatternEdit extends RoboActivity {

	private static final int ACTIVITY_PATTERN_EDIT = 1;

	@Inject
	private IUserSettings user_settings;

	@Override
	protected void onStart() {
		super.onStart();

		// Start editing
		editPattern();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ACTIVITY_PATTERN_EDIT:
			// Refresh contact list
			if (resultCode == RESULT_OK && data != null) {
				// TODO
				user_settings.defaultPattern(new Pattern().setPattern(data
						.getLongArrayExtra(PatternEdit.EXTRA_KEY_PATTERN)));
			}
			finish();
			break;
		}
	}

	private void editPattern() {
		// TODO
		Intent i = new Intent(this, PatternEdit.class);
		i.putExtra(PatternEdit.EXTRA_KEY_PATTERN, user_settings
				.defaultPattern().asArray());
		startActivityForResult(i, ACTIVITY_PATTERN_EDIT);
	}
}
