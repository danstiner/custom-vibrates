package com.danielstiner.vibrates.notify;

import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Provider;

import android.content.Context;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityHandler {
	
	private static final String GOOGLE_VOICE_PACKAGE = "com.google.android.apps.googlevoice";
	private static final String GOOGLE_VOICE_TYPE = VibrateNotify.particularizeType(VibrateNotify.TYPE_SMS, "google_voice");
	
	private Provider<VibrateNotify> notify_provider;
    
	@Inject
    public AccessibilityHandler(Provider<VibrateNotify> notify_provider)
    {
    	this.notify_provider = notify_provider;
    }
	
	public void handle(AccessibilityEvent event, Context context) {
		// TODO Auto-generated method stub\
		android.app.Notification notification = (android.app.Notification) event.getParcelableData();
		//Toast.makeText(getBaseContext(), event.getPackageName(), Toast.LENGTH_SHORT).show();
		//Parcelable p = event.getParcelableData();
		// Parse our accessibility event
		//new Notification
		if(event.getPackageName().toString().equals(GOOGLE_VOICE_PACKAGE))
			handleGoogleVoice(event, context);
		
	}
	
	private void handleGoogleVoice(AccessibilityEvent event, Context context) {
		if(event.getText().size() < 1){
			Ln.e("Bad Google Voice notifcation: no event text!");
			return;
		}
		
		// Split the notification up
		String[] message_parts = event.getText().get(0).toString().split(":", 2);
		if(message_parts.length != 2)
		{
			Ln.e("Bad Google Voice notifcation: no colon separated name:message!");
			return;
		}
		
		String name = message_parts[0];
		String text = message_parts[1];
		notify_provider.get().identifier(name).type(GOOGLE_VOICE_TYPE).extra(text).fire(context);
	}

	

}
