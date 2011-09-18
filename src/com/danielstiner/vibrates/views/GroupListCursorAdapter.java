package com.danielstiner.vibrates.views;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.database.IManager;
import com.google.inject.Injector;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class GroupListCursorAdapter extends CursorAdapter {
	
	// For view management
	private int layout = R.layout.group_list_row;

	public GroupListCursorAdapter(Context context, Cursor c, IManager manager, Injector injector) {
		super(context, c);
		
		//this.manager = manager;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);

		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {

		// Bind in contact name
		TextView title_text = (TextView)v.findViewById(R.id.group_list_row_title);
		if (title_text != null) {
			title_text.setText(c.getString(c.getColumnIndexOrThrow(ContactsContract.Groups.TITLE)));
		}
		
//		// Bind in contacts count
//		TextView count_text = (TextView)v.findViewById(R.id.group_list_row_size);
//		if (count_text != null) {
//			int count = c.getInt(c.getColumnIndexOrThrow(ContactsContract.Groups.SUMMARY_COUNT));
//			count_text.setText(Integer.toString(count));
//		}
	}
}

