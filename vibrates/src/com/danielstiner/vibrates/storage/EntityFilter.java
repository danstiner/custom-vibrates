package com.danielstiner.vibrates.storage;

public class EntityFilter implements IEntityFilter {

	private String mType = "";
	private boolean mInitialized = false;;

	@Override
	public IEntityFilter setType(String type) {
		mType = type;
		this.onUpdate();
		return this;
	}

	@Override
	public String getType() {
		return mType;
	}

	private void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoaderId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isInitialized() {
		return mInitialized;
	}

	@Override
	public void initialize() {
		mInitialized = true;
	}

}
