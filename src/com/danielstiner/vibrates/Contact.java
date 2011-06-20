package com.danielstiner.vibrates;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.util.Pair;

public class Contact {

	private CustomContactManager _ccm;

	private Long _id;

	private String _displayName;

	private String _lookupKey;

	private int _timesContacted;

	private List<Pair<String,long[]>> _vibrates;

	public Contact(String lookupKey, long id, CustomContactManager ccm) {
		_lookupKey = lookupKey;
		_id = id;
		_ccm = ccm;
	}

	public Long getId() {
		return _id;
	}

	public String getLookupKey() {
		return _lookupKey;
	}

	public Uri getLookupUri() {
		return ContactsContract.Contacts.getLookupUri(_id, _lookupKey);
	}

	public String getDisplayName() {
		populateInfo();
		return _displayName;
	}

	public InputStream openPhotoInputStream() {
		return _ccm.getPhotoStream(getId());
	}
	
	public long[] getDefaultPattern() {
		//_patterns = _ccm.getVibratePatterns(getRowId());
		// TODO Auto-generated method stub
		return new long[]{};
	}

	public long[] getPattern(String mimetype) {
		// TODO Auto-generated method stub
		return new long[]{0, 1000};
	}

	public List<Pair<String,long[]>> getPatterns() {
		populateVibrates();
		return _vibrates;
	}
	public int getTimesContacted() {
		populateInfo();
		return _timesContacted;
	}
	public Date getLastTimeContacted() {
		populateInfo();
		return null;
	}
	
	private void populateInfo() {
		Map<String,String> info = _ccm.getContactInfo(_lookupKey, _id);
		
		_displayName = info.get(ContactsContract.Contacts.DISPLAY_NAME);
		_timesContacted = Integer.parseInt(info.get(ContactsContract.Contacts.TIMES_CONTACTED));
		//_displayName = info.get(ContactsContract.Contacts.LAST_TIME_CONTACTED);
	}
	private void populateVibrates() {
		_vibrates = _ccm.getVibrates(_id);
	}
}
