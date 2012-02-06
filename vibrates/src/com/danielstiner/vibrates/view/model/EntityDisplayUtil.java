package com.danielstiner.vibrates.view.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.QuickContactBadge;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.storage.IManager;

public class EntityDisplayUtil {
	public static void setupBadge(QuickContactBadge badge, Entity e,
			IManager manager) {

		Uri lookup_uri = manager.getContactUri(e);

		badge.assignContactUri(lookup_uri);

		badge.setImageDrawable(Drawable.createFromStream(
				manager.getPhotoStream(e), "contactphoto"));

	}
}
