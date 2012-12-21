package com.danielstiner.vibrates.notify;

import roboguice.receiver.RoboBroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.inject.Inject;

public class IntentReciever extends RoboBroadcastReceiver {

	@Inject
	IIntentHandler handler;

	@Override
	protected void handleReceive(Context context, Intent intent) {
		// Handle the action
		handler.handle(intent.getAction(), intent.getExtras(), context);
	}

}
