package com.danielstiner.vibrates.notify;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Vibrator;

import com.danielstiner.vibrates.Pattern;

public abstract class AbstractPatternPlayer extends TimerTask {

	public abstract AbstractPatternPlayer pattern(Pattern p);

	public abstract AbstractPatternPlayer vibrator(Vibrator vibrator);

	public abstract AbstractPatternPlayer timer(Timer timer);

}
