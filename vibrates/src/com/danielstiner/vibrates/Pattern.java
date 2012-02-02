package com.danielstiner.vibrates;

import java.util.ArrayList;
import java.util.List;

public class Pattern {

	private long[] pattern;

	public List<Long> asList() {
		ArrayList<Long> list = new ArrayList<Long>();

		if (pattern == null)
			return list;

		for (int i = 0; i < pattern.length; i++) {
			list.add(pattern[i]);
		}

		return list;
	}

	public Pattern pattern(long[] pattern) {
		this.pattern = pattern;
		return this;
	}

	public long[] asArray() {
		return pattern.clone();
	}

}
