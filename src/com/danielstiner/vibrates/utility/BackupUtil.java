package com.danielstiner.vibrates.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import roboguice.util.Ln;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.danielstiner.vibrates.Entity;
import com.danielstiner.vibrates.database.IManager;
import com.google.inject.Inject;

public class BackupUtil {

	private static final String BACKUP_FILE_NAME = "backup.csv";
	
	private static final String LINE_END = "\n";
	
	private static final String FIELD_SEPARATOR = "\t";
	
	private static final String PATTERN_SEPARATOR = ",";

	@Inject
	private Application mContext;
	
	@Inject IManager mManager;
	
	public BackupUtil()
	{
	}
	
	/**
	 * 
	 * @return Success?
	 */
	public boolean Backup() {
		// Do Backup

		if (!isExternalStorageAvaible(true))
			return false;

		File dir = getBackupDirPath(mContext);
		
		
		try {
			// Get all contacts
			Cursor c = mManager.getEntities();
			
			// Write them out
			if(!c.moveToFirst())
				return false;

			// build the directory structure, if needed.
			dir.mkdirs();
						
			FileWriter f = new FileWriter(new File(dir, BACKUP_FILE_NAME), false);

			do {
				Entity entity = mManager.getEntity(c);
				
				long[] pattern = mManager.getPattern(entity, "");
				
				Uri contactUri = mManager.getContactUri(entity);
				
				String kind = mManager.getKind(entity);
				
				f.write(formatEntityOut(kind, contactUri, pattern));
				
			} while(c.moveToNext());
			
			f.close();
			
		} catch (FileNotFoundException e) {
			Ln.e(e);
			return false;
		} catch (IOException e) {
			Ln.e(e);
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @return Success?
	 */
	public static boolean Restore() {
		return false;
	}
	
	private static String formatEntityOut(String kind, Uri contactUri, long[] pattern)
	{
		StringBuilder sb = new StringBuilder();
		
		if(kind != null)
			sb.append(kind.replaceAll(LINE_END, " ").replaceAll(FIELD_SEPARATOR, ""));
		sb.append(FIELD_SEPARATOR);
		
		if(contactUri != null)
			sb.append(contactUri.toString().replaceAll(LINE_END, " ").replaceAll(FIELD_SEPARATOR, ""));
		sb.append(FIELD_SEPARATOR);
		
		if(pattern != null)
		{
			for(int i = 0; i < pattern.length-1; i++)
			{
				sb.append(pattern[i]);
				sb.append(PATTERN_SEPARATOR);
			}
			
			if(pattern.length > 0)
				sb.append(pattern[pattern.length-1]);
		}
		
		sb.append(LINE_END);
		
		return sb.toString();
	}

	private static boolean isExternalStorageAvaible(boolean writable) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()))
			return true;

		else if (!writable
				&& Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment
						.getExternalStorageState()))
			return true;
		else
			return false;
	}

	private static File getBackupDirPath(Context context) {
		// Simple way
		// File sdCardBackupDir = context.getExternalFilesDir("backup");
		// File file = new File(sdCardBackupDir, "filename");

		// Compatible back to api 7
		String packageName = context.getApplicationContext().getPackageName();
		File externalPath = Environment.getExternalStorageDirectory();
		File appFiles = new File(externalPath.getAbsolutePath()
				+ "/Android/data/" + packageName + "/files");

		return appFiles;
	}

}
