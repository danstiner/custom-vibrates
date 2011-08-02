package com.danielstiner.vibrates.views;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.database.IIdentifierManager;
import com.google.inject.Inject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;

public class UnknownIdentifierList extends CoreListActivity {
	
	@Inject IIdentifierManager identifier_manager;
	
	private static final int CONTENT_VIEW = R.layout.identifier_list;

	private static final int CONTEXTMENU_DELETE_ID = 0;

	private Cursor mCursor;
	
	private IdentifierListCursorAdapter mCursorAdapter;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.contactlistmenu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(Menu.NONE, CONTEXTMENU_DELETE_ID, Menu.NONE, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case CONTEXTMENU_DELETE_ID:
//			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
//					.getMenuInfo();
//			// TODO Not a great method for linking row in list to database delete
//			mCursor.moveToPosition((int)info.position);
//			
//			ccm.remove(ccm.fromCursor(mCursor));
//			fillContactList();
//			return true;
//		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

//		mCursor.moveToPosition(position);
//		
//		Entity entity = ccm.fromCursor(mCursor);
//
//		editEntity(entity);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		Bundle extras = data.getExtras();

//		switch (requestCode) {
//		case ACTIVITY_EDIT:
//			// Refresh contact list
//			if (resultCode == RESULT_OK)
//				fillContactList();
//			break;
//		case ACTIVITY_PICK_CONTACT:
//			if (resultCode == RESULT_OK) {
//				Uri contactpath = data.getData();	
//				editEntity(ccm.createFromContactUri(contactpath));
//			} else {
//				// TODO: gracefully handle failure
//				Log.w(DEBUG_TAG, "Warning: activity result not ok");
//			}
//			break;
//		}
	}

	
	private void associate() {
		// Show a selectable list of entities (contacts?) to associate this identifier with
		// FIXME 
	}

	@Override
	protected void fillList() {
		if(mCursor != null)
			mCursor.close();
		
		// Get all of the notes from the database and create the item list
		mCursor = identifier_manager.getOrphans();
		startManagingCursor(mCursor);

		// Now create an array adapter and set it to display using our row
		setListAdapter(mCursorAdapter = new IdentifierListCursorAdapter(this, mCursor));

	}

	@Override
	protected int getContentView() {
		return CONTENT_VIEW;
	}

	@Override
	protected void initEmptyView() {
		// TODO Auto-generated method stub
		
	}

	
}
