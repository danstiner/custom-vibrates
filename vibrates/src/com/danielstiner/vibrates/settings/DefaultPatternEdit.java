package com.danielstiner.vibrates.settings;

import roboguice.activity.RoboActivity;
import android.content.Intent;

import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.view.PatternEditActivity;
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
						.getLongArrayExtra(PatternEditActivity.EXTRA_KEY_PATTERN)));
			}
			finish();
			break;
		}
	}

	private void editPattern() {
		// TODO
		Intent i = new Intent(this, PatternEditActivity.class);
		i.putExtra(PatternEditActivity.EXTRA_KEY_PATTERN, user_settings
				.defaultPattern().asArray());
		startActivityForResult(i, ACTIVITY_PATTERN_EDIT);
	}
}
