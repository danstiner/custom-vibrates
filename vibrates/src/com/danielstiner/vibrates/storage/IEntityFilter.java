package com.danielstiner.vibrates.storage;

public interface IEntityFilter {

	IEntityFilter setType(String type);

	String getType();

	int getLoaderId();

	boolean isInitialized();
	
	void initialize();
	

}
