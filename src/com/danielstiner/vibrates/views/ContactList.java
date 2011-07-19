package com.danielstiner.vibrates.views;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.R.id;
import com.danielstiner.vibrates.R.layout;
import com.danielstiner.vibrates.R.menu;
import com.danielstiner.vibrates.R.string;
import com.danielstiner.vibrates.database.EntityManager;
import com.danielstiner.vibrates.managers.Manager;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.provider.ContactsContract.CommonDataKinds.Email;

public class ContactList extends ListActivity {

	private static final int CONTEXTMENU_DELETE_ID = 1;

	private static final int OPTIONS_INSERT_ID = 2;

	private static final int ACTIVITY_CREATE = 3;

	private static final int ACTIVITY_EDIT = 4;
	
	private static final int ACTIVITY_PICK_CONTACT = 5;

	private static final String DEBUG_TAG = "Vibrates";

	
	private EntityManager ccm;

	private Cursor mContactsCursor;
	
	private EntityListCursorAdapter mContactsCursorAdapter;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlist);

		// Setup the contact list back-end and populate list
		ccm = new EntityManager(this);
		
		fillContactList();

		// Register for context events from the list
		registerForContextMenu(getListView());
	
		initEmptyView();
	}
	
	private void initEmptyView() {
		
		final Button newContactBtn = (Button) findViewById(R.id.empty_add_contact_button);
		newContactBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newContact();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.contactlistmenu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case OPTIONS_INSERT_ID:
			newContact();
			//return true;
			break;
		case R.id.menu_add:
			newContact();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, CONTEXTMENU_DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case CONTEXTMENU_DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			// TODO Not a great method for linking row in list to database delete
			mContactsCursor.moveToPosition((int)info.position);
			
			ccm.remove(ccm.fromCursor(mContactsCursor));
			fillContactList();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		mContactsCursor.moveToPosition(position);
		
		Entity entity = ccm.fromCursor(mContactsCursor);

		editEntity(entity);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		Bundle extras = data.getExtras();

		switch (requestCode) {
		case ACTIVITY_EDIT:
			// Refresh contact list
			if (resultCode == RESULT_OK)
				fillContactList();
			break;
		case ACTIVITY_PICK_CONTACT:
			if (resultCode == RESULT_OK) {
				Uri contactpath = data.getData();	
				editEntity(ccm.createFromContactUri(contactpath));
			} else {
				// TODO: gracefully handle failure
				Log.w(DEBUG_TAG, "Warning: activity result not ok");
			}
			break;
		}
	}

	private void fillContactList() {
		// TODO Auto-generated method stub
		if(mContactsCursor != null)
			mContactsCursor.close();
		// Get all of the notes from the database and create the item list
		mContactsCursor = ccm.getAll();
		startManagingCursor(mContactsCursor);

		// Now create an array adapter and set it to display using our row
		mContactsCursorAdapter = new EntityListCursorAdapter(this, mContactsCursor);
		setListAdapter(mContactsCursorAdapter);

	}

	
	private void newContact() {
		// TODO First ask if we should be adding a service group or other special contact
		// or actual contact
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
			ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, ACTIVITY_PICK_CONTACT);

	}
	private void editEntity(Entity contact) {
		// Start contact edit activity
		Intent i = new Intent(this, EditEntity.class);
		i.putExtra(Manager.ENTITY_ID_KEY, contact.entityid());
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	private class EntityListCursorAdapter extends CursorAdapter { // implements Filterable {

		// For view management
		private Context context;
		private int layout = R.layout.contactrow;

		public EntityListCursorAdapter(Context context, Cursor c) {
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

			final LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(layout, parent, false);

			return v;
		}
		
		

		@Override
		public void bindView(View v, Context context, Cursor c) {

			
			
			// Lets find some info about this contact
			Entity entity = ccm.fromCursor(c);
			
			// Bind in contact name
			TextView name_text = (TextView) v.findViewById(R.id.contactrow_name);
			if (name_text != null) {
				name_text.setText(ccm.getDisplayName(entity));
			}
			
			// Bind in contact photo
			ImageView pic = (ImageView) v.findViewById(R.id.contactrow_image);
			if (pic != null) {
				pic.setImageDrawable(Drawable.createFromStream(ccm.getPhotoStream(entity), "contactphoto"));
			}
			
			// Bind in default vibrate pattern
			VibratePatternView pattern_view = (VibratePatternView) v.findViewById(R.id.contactrow_defaultpattern);
			if (pattern_view != null) {
				pattern_view.setPattern(ccm.getPattern(entity));
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

}
