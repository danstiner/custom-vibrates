package com.danielstiner.vibrates.settings;

import roboguice.activity.RoboActivity;
import android.app.Activity;
import android.widget.Toast;

import com.danielstiner.vibrates.utility.BackupUtil;
import com.google.inject.Inject;

public class Backup extends RoboActivity {

	@Inject private BackupUtil backup_util;
	
	@Override
	protected void onStart() {
		super.onStart();
		
		boolean result = backup_util.Backup();
		
		if(!result)
		{
			// Failed
			Toast.makeText(this, "Backup Failed!", Toast.LENGTH_LONG).show();
			this.setResult(Activity.RESULT_CANCELED);
			this.finish();
		}
		else
		{
			// Done.
			Toast.makeText(this, "Successful Backup", Toast.LENGTH_LONG).show();
			this.setResult(Activity.RESULT_OK);
			this.finish();
		}
	}
	
}
