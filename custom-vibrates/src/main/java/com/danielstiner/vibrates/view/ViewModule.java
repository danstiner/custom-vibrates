package com.danielstiner.vibrates.view;

import com.danielstiner.vibrates.view.fragments.IListEntitiesFragment;
import com.danielstiner.vibrates.view.fragments.ListEntitiesFragment;
import com.google.inject.AbstractModule;

public class ViewModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IListEntitiesFragment.class).to(ListEntitiesFragment.class);
	}

}
