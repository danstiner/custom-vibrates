package com.danielstiner.vibrates.service;

import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.notify.VibrateNotify.Kind;

interface IPatternPlayer {

	void play(Pattern p, Kind kind, Runnable onFinish);

	void cancel();

}
