package com.danielstiner.vibrates.view.model;

import android.net.Uri;
import android.widget.QuickContactBadge;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.model.IDataModel;

public class EntityDisplayUtil {
	public static void setupBadge(QuickContactBadge badge, Entity e,
			IDataModel manager) {

		Uri lookup_uri = manager.getContactUri(e);

		badge.assignContactUri(lookup_uri);

		badge.setImageDrawable(manager.getPicture(e));

	}
}
