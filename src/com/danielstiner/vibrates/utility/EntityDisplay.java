package com.danielstiner.vibrates.utility;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.database.IManager;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.QuickContactBadge;

public class EntityDisplay {
	public static void setupBadge(QuickContactBadge badge, Entity e,
			IManager manager) {
		
		Uri lookup_uri = manager.getContactUri(e);
		
		badge.assignContactUri(lookup_uri);
		
		badge.setImageDrawable(Drawable.createFromStream(manager.getPhotoStream(e), "contactphoto"));
		
	}
}
