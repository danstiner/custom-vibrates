package com.danielstiner.vibrates.view.fragments;

import android.database.Cursor;
import android.support.v4.app.Fragment;

import com.danielstiner.vibrates.R;
import com.danielstiner.vibrates.model.IDataModel;
import com.danielstiner.vibrates.view.model.GroupCursorAdapter;
import com.google.inject.Inject;

public class PickGroup extends Fragment {

	private static final String NS = com.danielstiner.vibrates.Vibrates.NS
			+ "." + "views";
	private static final String CLASSNAME = NS + "." + "PickGroup";

	protected static final String EXTRA_KEY_ID = CLASSNAME + ".groupid";

	private static final int CONTENT_VIEW = R.layout.entity_list;

	@Inject
	private IDataModel manager;

	// @Inject
	// private IIdentifierManager identifier_manager;

	private Cursor mGroupsCursor;

	private GroupCursorAdapter mGroupsCursorAdapter;

	// @Override
	// protected void fillList() {
	// // Close any existing cursors
	// if (mGroupsCursor != null)
	// mGroupsCursor.close();
	//
	// // Get all of our current groups
	// Cursor appGroupsCursor = manager.getEntities(Entity.TYPE_GROUP);
	//
	// // Create exclusion filter
	// String excludeIdsCSV = "";
	// if (appGroupsCursor.moveToFirst()) {
	// do {
	// Entity group = manager.getEntity(appGroupsCursor);
	// Cursor groupIds = identifier_manager.get(group,
	// IdentifierManager.KIND_CONTACTS_GROUP_ID);
	//
	// if (groupIds.moveToFirst()) {
	// excludeIdsCSV += ","
	// + identifier_manager.identifierFromCursor(groupIds);
	// }
	//
	// groupIds.close();
	//
	// } while (appGroupsCursor.moveToNext());
	//
	// // Drop unwanted leading comma
	// if (excludeIdsCSV.length() > 0)
	// excludeIdsCSV = excludeIdsCSV.substring(1);
	// }
	// appGroupsCursor.close();
	//
	// // Get all of the system groups
	// mGroupsCursor = getContentResolver().query(
	// ContactsContract.Groups.CONTENT_SUMMARY_URI,
	// new String[] { ContactsContract.Groups.TITLE,
	// ContactsContract.Groups._ID,
	// ContactsContract.Groups.SUMMARY_COUNT },
	// ContactsContract.Groups._ID + " NOT IN (?)",
	// new String[] { excludeIdsCSV }, null);
	// startManagingCursor(mGroupsCursor);
	//
	// // Now create an array adapter and set it to display using our rows
	// mGroupsCursorAdapter = new GroupCursorAdapter(
	// (Context) this.getActivity(), mGroupsCursor, manager,
	// RoboGuice.getInjector((Context) this.getActivity()));
	// setListAdapter(mGroupsCursorAdapter);
	// }
	//
	// @Override
	// public void onListItemClick(ListView l, View v, int position, long id) {
	// super.onListItemClick(l, v, position, id);
	//
	// mGroupsCursor.moveToPosition(position);
	//
	// // Save state of our chosen group
	// Intent i = new Intent();
	// i.putExtra(EXTRA_KEY_ID, mGroupsCursor.getLong(mGroupsCursor
	// .getColumnIndexOrThrow(ContactsContract.Groups._ID)));
	// setResult(Activity.RESULT_OK, i);
	//
	// // return to the caller
	// finish();
	// }
	//
	// @Override
	// protected int getContentView() {
	// return CONTENT_VIEW;
	// }
	//
	// @Override
	// protected void initEmptyView() {
	// // Only text in the empty view, do nothing
	// }

}
