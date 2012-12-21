package com.danielstiner.vibrates.service;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(INotificationHandler.class).to(NoticationHandler.class);
		bind(IPatternPlayer.class).to(PatternPlayer.class);
	}

}
