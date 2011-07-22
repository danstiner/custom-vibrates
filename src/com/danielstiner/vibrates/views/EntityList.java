package com.danielstiner.vibrates.views;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import roboguice.inject.InjectView;
import roboguice.util.Ln;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.R.id;
import com.danielstiner.vibrates.R.layout;
import com.danielstiner.vibrates.R.menu;
import com.danielstiner.vibrates.R.string;
import com.danielstiner.vibrates.database.IEntityManager;
import com.google.inject.Inject;

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

public class EntityList extends CoreListActivity {
	
	@Inject IEntityManager entity_manager;
	
	@InjectView(R.id.empty_add_contact_button) Button newContactBtn;

	private static final int CONTEXTMENU_DELETE_ID = 1;
	private static final int OPTIONS_INSERT_ID = 2;
	//private static final int ACTIVITY_CREATE = 3;
	private static final int ACTIVITY_EDIT = 4;
	
	private static final int CONTENT_VIEW = R.layout.contactlist;
	
	private static final int ACTIVITY_PICK_CONTACT = 5;

	private Cursor mContactsCursor;
	
	private EntityListCursorAdapter mContactsCursorAdapter;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
	}
	
	@Override
	protected void initEmptyView() {
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
			
			entity_manager.remove(entity_manager.fromCursor(mContactsCursor));
			fillList();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		mContactsCursor.moveToPosition(position);
		
		Entity entity = entity_manager.fromCursor(mContactsCursor);

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
				fillList();
			break;
		case ACTIVITY_PICK_CONTACT:
			if (resultCode == RESULT_OK) {
				Uri contactpath = data.getData();	
				editEntity(entity_manager.createFromContactUri(contactpath));
			} else {
				// TODO: gracefully handle failure
				Ln.d("Warning: activity result not ok");
			}
			break;
		}
	}

	@Override
	protected void fillList() {
		// TODO Auto-generated method stub
		if(mContactsCursor != null)
			mContactsCursor.close();
		// Get all of the notes from the database and create the item list
		mContactsCursor = entity_manager.getAll();
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
		i.putExtra(Entity.ID_BUNDLE_KEY, contact.entityid());
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	protected int getContentView() {
		return CONTENT_VIEW;
	}
}
