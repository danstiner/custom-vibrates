package com.danielstiner.vibrates.notify;

import com.google.inject.AbstractModule;

public class NotifyModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(INotificationConstraints.class).to(NotificationConstraints.class);
		bind(IIntentHandler.class).to(IntentHandler.class);
		bind(AbstractPatternPlayer.class).to(PatternPlayer.class);
	}

}
