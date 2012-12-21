package com.danielstiner.vibrates.model.internal;

class InternalStorageUtil {

	private static int msNextLoaderId = 0;

	/**
	 * Gives back a globally unique loader id
	 * 
	 * @return Id globally unique in this app
	 */
	static int getNextLoaderId() {
		return msNextLoaderId++;
	}

}
