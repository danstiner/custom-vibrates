package com.danielstiner.vibrates.view.fragments;

import com.danielstiner.vibrates.Entity.Kind;
import com.danielstiner.vibrates.view.model.OnEntitySelectedListener;

public interface IListEntitiesFragment extends OnEntitySelectedListener {

	void setKind(Kind kind);

}
