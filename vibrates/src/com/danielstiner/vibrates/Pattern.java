package com.danielstiner.vibrates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pattern {

	public static final Pattern NONE;
	static {
		Pattern p = new Pattern();
		p.setPattern(new long[] { 0, 0 });
		NONE = p;
	}

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

	public Pattern setPattern(long[] pattern) {
		this.pattern = pattern;
		return this;
	}

	public long[] asArray() {
		return pattern.clone();
	}

	public static Pattern fromArray(long[] pattern) {
		return new Pattern().setPattern(pattern);
	}

	public static Pattern fromString(String pattern_packed) {

		if (pattern_packed == null)
			return null;

		String[] pattern_parts = pattern_packed.split(",");
		long[] pattern = new long[pattern_parts.length];

		try {
			for (int i = 0; i < pattern.length; i++)
				pattern[i] = Long.parseLong(pattern_parts[i].trim());
		} catch (Exception e) {
			return null;
		}

		return new Pattern().setPattern(pattern);
	}

	@Override
	public String toString() {

		String pattern_str = Arrays.toString(pattern);

		return pattern_str.substring(1, pattern_str.length() - 1);
	}

}
