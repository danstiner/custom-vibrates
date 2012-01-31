package com.danielstiner.vibrates.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnStartReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Schedule recurring sync
		SyncAlarm.start(context);
	}

}
