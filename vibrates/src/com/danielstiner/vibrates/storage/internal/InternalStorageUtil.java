package com.danielstiner.vibrates.storage.internal;

class InternalStorageUtil {
	
	private static int msNextLoaderId = 0;

	/** Gives back a globally unique loader id
	 *
	 * @return
	 */
	static int getNextLoaderId() {
		return msNextLoaderId++;
	}

}
