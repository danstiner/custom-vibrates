package com.danielstiner.vibrates.view.model;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.view.PatternView;
import com.google.inject.Inject;

public class EntityCursorAdapter extends CursorAdapter {

	@Inject
	private IDataModel mDataModel;

	// For view management
	private int layout = R.layout.entity_list_row;

	@Inject
	public EntityCursorAdapter(Context context) {
		super(context, null, 0);
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
		Entity entity = mDataModel.entityFromCursor(c);

		// Bind in contact name
		TextView name_text = (TextView) v
				.findViewById(R.id.entitylist_row_name);
		if (name_text != null) {
			name_text.setText(entity.getName());
		}

		// Bind in contact photo
		ImageView entity_pic = (ImageView) v
				.findViewById(R.id.entitylist_row_image);
		if (entity_pic != null) {
			entity_pic.setImageDrawable(mDataModel.getPicture(entity));
		}

		// Bind in default vibrate pattern
		PatternView pattern_view = (PatternView) v
				.findViewById(R.id.entitylist_row_pattern);
		if (pattern_view != null) {
			pattern_view.setPattern(entity.getPattern());
		}
	}

	public Entity getEntity(int position) {
		Cursor c = this.getCursor();

		if (c == null)
			return null;

		// Save current position
		int pos = c.getPosition();

		// Try move to given position
		if (!c.move(position)) {
			c.move(pos);
			return null;
		}

		// Grab entity at position, move back and return
		Entity e = mDataModel.entityFromCursor(c);
		c.move(pos);
		return e;
	}
}
