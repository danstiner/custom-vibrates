package com.danielstiner.vibrates.view.fragments;

import com.danielstiner.vibrates.Pattern;

public interface IPatternEdit {

	/**
	 * 
	 * @param pattern
	 *            Pattern to edit
	 * @param play
	 *            Initially play pattern
	 */
	void setPattern(Pattern pattern, boolean play);

	/**
	 * 
	 * @return Current pattern being edited, or Pattern.NONE if no pattern is
	 *         yet create/set
	 */
	Pattern getPattern();

}
