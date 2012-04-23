package com.danielstiner.vibrates.service;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Vibrator;

import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.notify.VibrateNotify;
import com.danielstiner.vibrates.notify.VibrateNotify.Kind;
import com.google.inject.Inject;

public class PatternPlayer implements IPatternPlayer {

	@Inject
	private Vibrator vibrator;

	@Inject
	private Timer timer;

	@Override
	public void play(Pattern pattern, VibrateNotify.Kind kind, Runnable onFinish) {

		// Boolean to alternate between wait and vibrate times in the pattern
		// First number in pattern is a wait time
		boolean is_wait_period = true;

		long wait_time = 0;
		
		if(pattern == null)
			return;

		// Schedule parts
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

		// Final call to stop vibrating
		timer.schedule(new PatternPlayerPart(true, 0), wait_time);

		// Actual final call to trigger on finish callback
		timer.schedule(new OnFinishTask(pattern, kind, onFinish), wait_time);
	}

	@Override
	public void cancel() {
		timer.cancel();
		timer.purge();

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

	private class OnFinishTask extends TimerTask {
		
		private Runnable mOnFinish;
		private Pattern mPattern;
		private Kind mKind;

		public OnFinishTask(Pattern pattern, Kind kind, Runnable onFinish) {
			mPattern = pattern;
			mKind = kind;
			mOnFinish = onFinish;
		}

		@Override
		public void run() {
			if(mKind == VibrateNotify.Kind.Continuous)
				PatternPlayer.this.play(mPattern, mKind, mOnFinish);
			else
				mOnFinish.run();
		}

	}

}
