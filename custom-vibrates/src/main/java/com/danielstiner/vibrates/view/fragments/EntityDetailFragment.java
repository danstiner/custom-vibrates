package com.danielstiner.vibrates.view.fragments;

import roboguice.fragment.RoboFragment;
import roboguice.inject.ContextScopedProvider;
import roboguice.inject.InjectView;
import roboguice.util.Ln;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.Pattern;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.model.StorageUtil;
import com.danielstiner.vibrates.view.components.PatternEditView;
import com.danielstiner.vibrates.view.model.OnEntitySelectedListener;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EntityDetailFragment extends RoboFragment implements
		OnEntitySelectedListener {

	@Inject
	private Application mAppContext;

	@Inject
	private ContextScopedProvider<IDataModel> mModelProvider;

	@InjectView(R.id.contact_badge)
	private QuickContactBadge mQuickContactBadge;

	@InjectView(R.id.name)
	private TextView mNameTextView;

	@InjectView(R.id.type)
	private TextView mTypeTextView;

//	@InjectView(R.id.pattern)
//	private PatternEditView mPatternEditView;

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_entity_detail, container, false);
	}

	@Override
	public void onEntitySelected(Entity e) {
		mNameTextView.setText(e.getName());
		mTypeTextView.setText(e.getKind().toDisplayString());

		IDataModel model = mModelProvider.get(mAppContext);
		StorageUtil.assignPicture(mQuickContactBadge, model);

	}
}
