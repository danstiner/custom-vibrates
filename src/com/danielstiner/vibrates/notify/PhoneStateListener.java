package com.danielstiner.vibrates.notify;

import com.google.inject.Provider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.danielstiner.vibrates.utility.NotificationTypes;
import com.google.inject.Inject;

public class PhoneStateListener extends android.telephony.PhoneStateListener {
	
//	@Override
//	public void onCallStateChanged(int state, String incomingNumber) {
//		
//		// Check if we are currently ringing
//		if(state == TelephonyManager.CALL_STATE_RINGING) {
//			
//		}
//		
//		// Check if we just answered all incoming calls
//		if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
//			handleOffhook(incomingNumber);
//		}
//		
//		super.onCallStateChanged(state, incomingNumber);
//	}
//	
//	
//	private void handleIncomingCall(String incomingNumber) {
//		// Check if we are currently ringing
//		
//		// Hand off handling of this event
//		
//		VibrateNotify n = new VibrateNotify(new IntentProvider());
//		n.identifier(incomingNumber).type(NotificationTypes.VOICE_PHONECALL);
//		n.fire(this.);
//		
//	}
//	
//	private void handleOffhook(String number) {
//		// Stop any ongoing vibrate notifications
//		//  by sending appropriate intent to vibratr service
//		
//	}
//	
//	private class IntentProvider implements Provider<Intent>
//	{
//		@Override
//		public Intent get() {
//			return new Intent();
//		}
//	}
}
