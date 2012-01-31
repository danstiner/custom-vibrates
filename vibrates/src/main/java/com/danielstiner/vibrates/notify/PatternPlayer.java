package com.danielstiner.vibrates.notify;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Vibrator;

import com.danielstiner.vibrates.Pattern;

public class PatternPlayer extends AbstractPatternPlayer {

	private Pattern pattern;
	private Vibrator vibrator;
	private Timer timer;

	@Override
	public void run() {
		// Check that we have a timer and pattern
		if (pattern == null || timer == null || vibrator == null)
			return;

		// Boolean to alternate between wait and vibrate times in the pattern
		// First number in pattern is a wait time
		boolean is_wait_period = true;

		long wait_time = 0;

		for (Long l : pattern.asList()) {

			timer.schedule(new PatternPlayerPart(is_wait_period, l), wait_time);

			if (is_wait_period == true) {
				// Queue wait time slice
				is_wait_period = false;
			} else {
				// Queue vibrate time slice
				is_wait_period = true;
				// vibrator.vibrate(l);
			}

			wait_time += l;

		}

		// Final call to stop everything
		timer.schedule(new PatternPlayerPart(true, 0), wait_time);

	}

	private class PatternPlayerPart extends TimerTask {

		private boolean isWaitPeriod;
		private long length;

		public PatternPlayerPart(boolean isWaitPeriod, long length) {
			this.isWaitPeriod = isWaitPeriod;
			this.length = length;
		}

		@Override
		public void run() {
			if (isWaitPeriod == true) {
				// wait time slice
				vibrator.cancel();
			} else {
				// vibrate time slice
				vibrator.vibrate(length);
			}
		}

	}

	@Override
	public AbstractPatternPlayer pattern(Pattern pattern) {
		this.pattern = pattern;
		return this;
	}

	@Override
	public AbstractPatternPlayer vibrator(Vibrator vibrator) {
		this.vibrator = vibrator;
		return this;
	}

	@Override
	public AbstractPatternPlayer timer(Timer timer) {
		this.timer = timer;
		return this;
	}

}
