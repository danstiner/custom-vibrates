package com.danielstiner.vibrates.views;

import roboguice.inject.InjectView;
import roboguice.util.Ln;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.database.IEntityManager;
import com.danielstiner.vibrates.database.IManager;
import com.danielstiner.vibrates.settings.Preferences;
import com.google.inject.Inject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class EntityList extends CoreListActivity {
	
	@Inject IManager manager;
	
	@Inject IEntityManager entity_manager;
	
	@InjectView(R.id.entitylist_empty_automajic) Button automajicButton;

	private static final int CONTEXTMENU_DELETE_ID = 1;
	private static final int OPTIONS_INSERT_ID = 2;
	//private static final int ACTIVITY_CREATE = 3;
	private static final int ACTIVITY_EDIT = 4;
	
	private static final int CONTENT_VIEW = R.layout.entity_list;
	
	private static final int ACTIVITY_ADD_CONTACT_CHOOSE = 5;

	private Cursor mContactsCursor;
	
	private EntityListCursorAdapter mContactsCursorAdapter;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void initEmptyView() {
		automajicButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//newContact();
				// TODO FIXME Need to do some majic here
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
		case R.id.menu_settings:
			openPreferences();
			break;
		case R.id.entitylist_menu_add_contact:
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

		switch (requestCode) {
		case ACTIVITY_EDIT:
			// Refresh contact list
			if (resultCode == RESULT_OK)
				fillList();
			break;
		case ACTIVITY_ADD_CONTACT_CHOOSE:
			if (resultCode == RESULT_OK && data != null) {
				Uri contactpath = data.getData();	
				editEntity(manager.createFromContactUri(contactpath));
				fillList();
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
		//startManagingCursor(mContactsCursor);

		// Now create an array adapter and set it to display using our row
		mContactsCursorAdapter = new EntityListCursorAdapter(this, mContactsCursor, manager, getInjector());
		setListAdapter(mContactsCursorAdapter);
	}

	
	private void newContact() {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, ACTIVITY_ADD_CONTACT_CHOOSE);
	}
	
	private void openPreferences() {
		startActivity(new Intent(this, Preferences.class));
	}
	
	private void editEntity(Entity contact) {
		if(contact != null)
		{
			// Start contact edit activity
			Intent i = new Intent(this, EditEntity.class);
			i.putExtra(Entity.EXTRA_KEY_ID, contact.entityid().longValue());
			startActivityForResult(i, ACTIVITY_EDIT);
		}
	}

	@Override
	protected int getContentView() {
		return CONTENT_VIEW;
	}
}
