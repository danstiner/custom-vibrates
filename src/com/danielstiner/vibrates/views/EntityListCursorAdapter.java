package com.danielstiner.vibrates.views;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IManager;
import com.danielstiner.vibrates.database.IPatternManager;
import com.google.inject.Injector;

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

public class EntityListCursorAdapter extends CursorAdapter { // implements Filterable {

	protected IManager manager;
	protected IEntityManager entity_manager;
	
	//@InjectView(R.id.entitylist_row_name) TextView name_text;
	//@InjectView(R.id.entitylist_row_image) ImageView entity_pic;
	//@InjectView(R.id.entitylist_row_defaultpattern) VibratePatternView pattern_view;
	
	// For view management
	private int layout = R.layout.entity_list_row;

	public EntityListCursorAdapter(Context context, Cursor c, IManager manager, Injector injector) {
		super(context, c);
		
		this.manager = manager;
		
		// Inject manually
		this.entity_manager = injector.getInstance(IEntityManager.class);
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
		Entity entity = entity_manager.fromCursor(c);
		
		// Bind in contact name
		TextView name_text = (TextView)v.findViewById(R.id.entitylist_row_name);
		if (name_text != null) {
			name_text.setText(entity_manager.getDisplayName(entity));
		}
		
		// Bind in contact photo
		ImageView entity_pic = (ImageView)v.findViewById(R.id.entitylist_row_image);
		if (entity_pic != null) {
			entity_pic.setImageDrawable(Drawable.createFromStream(entity_manager.getPhotoStream(entity), "contactphoto"));
		}
		
		// Bind in default vibrate pattern
		VibratePatternView pattern_view = (VibratePatternView)v.findViewById(R.id.entitylist_row_pattern);
		if (pattern_view != null) {
			pattern_view.setPattern(manager.getPattern(entity, null));
		}
		
		// Bind in some top service specific patterns
		// TODO
		
	}
/*
	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (getFilterQueryProvider() != null) {
			return getFilterQueryProvider().runQuery(constraint);
		}

		StringBuilder buffer = null;
		String[] args = null;
		if (constraint != null) {
			buffer = new StringBuilder();
			buffer.append("UPPER(");
			buffer.append(People.NAME);
			buffer.append(") GLOB ?");
			args = new String[] { constraint.toString().toUpperCase() + "*" };
		}

		return context.getContentResolver().query(People.CONTENT_URI, null,
				buffer == null ? null : buffer.toString(), args,
				People.NAME + " ASC");
	}*/
}

