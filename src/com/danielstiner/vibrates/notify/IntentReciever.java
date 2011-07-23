package com.danielstiner.vibrates.notify;

import com.google.inject.Inject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class IntentReciever extends BroadcastReceiver {
    
    @Inject IIntentHandler handler;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Handle the action
		handler.handle(intent.getAction(), intent.getExtras(), context);
		
	}
	
}
