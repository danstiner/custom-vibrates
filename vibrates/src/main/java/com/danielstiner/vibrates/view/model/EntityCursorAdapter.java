package com.danielstiner.vibrates.view.model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.database.IManager;
import com.danielstiner.vibrates.view.fragments.PatternView;
import com.google.inject.Injector;

public class EntityCursorAdapter extends CursorAdapter {

	protected IManager manager;

	// For view management
	private int layout = R.layout.entity_list_row;

	public EntityCursorAdapter(Context context, Cursor c, IManager manager,
			Injector injector) {
		super(context, c);

		this.manager = manager;
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

		// Lets find some info about this contact
		Entity entity = manager.getEntity(c);

		// Bind in contact name
		TextView name_text = (TextView) v
				.findViewById(R.id.entitylist_row_name);
		if (name_text != null) {
			name_text.setText(manager.getDisplayName(entity));
		}

		// Bind in contact photo
		ImageView entity_pic = (ImageView) v
				.findViewById(R.id.entitylist_row_image);
		if (entity_pic != null) {
			entity_pic.setImageDrawable(Drawable.createFromStream(
					manager.getPhotoStream(entity), "contactphoto"));
		}

		// Bind in default vibrate pattern
		PatternView pattern_view = (PatternView) v
				.findViewById(R.id.entitylist_row_pattern);
		if (pattern_view != null) {
			pattern_view.setPattern(manager.getPattern(entity, null));
		}
	}
}
