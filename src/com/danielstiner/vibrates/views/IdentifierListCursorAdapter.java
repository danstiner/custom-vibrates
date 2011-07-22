package com.danielstiner.vibrates.views;

import com.danielstiner.vibrates.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;

public class IdentifierListCursorAdapter extends CursorAdapter {
	
	private static final int layout = R.layout.identifier_row;
	
	// For view management
	private Context context;
	

	public IdentifierListCursorAdapter(Context context, Cursor c) {
		super(context, c);
		this.context = context;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(layout, parent, false);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// Lets find some info about this lookup
		
		// Bind in contact name
		// FIXME re-enable
//		TextView ident_text = (TextView) v.findViewById(R.id.identifier_row_ident);
//		if (ident_text != null) {
//			ident_text.setText(IdentifierManager.identifierFromCursor(c));
//		}
//		
//		// Bind in default vibrate pattern
//		TextView pattern_view = (TextView) v.findViewById(R.id.identifier_row_kind);
//		if (pattern_view != null) {
//			name_text.setText(IdentifierManager.kindFromCursor(c));
//		}
		
		
	}
}
