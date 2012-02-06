package com.danielstiner.vibrates.storage.internal;

import java.util.ArrayList;
import java.util.List;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Entity.Entities;
import com.danielstiner.vibrates.storage.IEntityFilter;
import com.danielstiner.vibrates.storage.VibrateEntityProvider;

public class EntityFilter implements IEntityFilter {

	private Entity.Kind mKind;
	private boolean mInitialized = false;
	private int mLoaderId = InternalStorageUtil.getNextLoaderId();
	private String mSortOrder;

	@Override
	public IEntityFilter setKind(Entity.Kind kind) {
		mKind = kind;
		this.onUpdate();
		return this;
	}

	@Override
	public Entity.Kind getKind() {
		return mKind;
	}
	
	public IEntityFilter setSortOrder() {
		//mSortOrder;
		this.onUpdate();
		return this;
	}

	private void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoaderId() {
		return mLoaderId;
	}

	@Override
	public boolean isInitialized() {
		return mInitialized;
	}

	@Override
	public void initialize() {
		mInitialized = true;
	}

	@Override
	public void apply(IEntityFilterProviderTarget target) {
		
		target.setUri(Entities.CONTENT_URI);
		
		// Build selection
		String selection = "";
		String selJoiner = " AND ";
		List<String> selectionArgs = new ArrayList<String>();
		
		// Kind
		if(mKind != null) {
			selection += selJoiner + Entities.KIND + " = ?";
			selectionArgs.add(mKind.toString());
		}
		
		// TODO other selections
		
		// Apply built selection string
		if(selection.length() < selJoiner.length())
			target.setSelection(null);
		else
			target.setSelection(selection.substring(selJoiner.length()));
		
		// Apply arguments for selection, transform to array first
		String[] selectionArgsArr = new String[selectionArgs.size()];
		selectionArgs.toArray(selectionArgsArr);
		target.setSelectionArgs(selectionArgsArr);
		
		// TODO
		target.setSortOrder(mSortOrder);
		
		// Get all fields
		target.setProjection(null);
	}

}
