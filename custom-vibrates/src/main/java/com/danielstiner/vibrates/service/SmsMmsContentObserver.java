package com.danielstiner.vibrates.service;

import com.google.inject.Inject;

import android.os.Handler;

public class SmsMmsContentObserver extends AbstractSmsMmsContentObserver {

	@Inject
	public SmsMmsContentObserver(Handler handler) {
		super(handler);
	}
	
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		
		
	}

	
//	protected void querySMS() {
//		Toast.
//	}
//	    Cursor cur = getContentResolver().query(u, null, null, null, null);
//	    cur.moveToNext(); // this will make it point to the first record, which is the last SMS sent
//	    String type = cur.getString(cur.getColumnIndex("type"));
//	    String body = cur.getString(cur.getColumnIndex("body")); //content of sms
//	    String add = cur.getString(cur.getColumnIndex("address")); //phone num
//	    if (type.equals("1")) {
//	        if (add.equals(Test.SENDER)) {              
//	            String[] bodys = body.split(" ", 7);
//	            if (bodys[0].equals("test")) {
//	                test = true;
//	            }
//	            cat = bodys[1];
//	            level = bodys[2];
//	            urgency = bodys[3];
//	            certainty = bodys[4];
//	            carrier = bodys[5];
//	            message = bodys[6];
//	            final Intent intent = new Intent(context, AlertActivity.class);
//	            Bundle b = new Bundle();
//	            b.putString("title", cat);
//	            b.putString("certainty", certainty);
//	            b.putString("urgency", urgency);
//	            b.putString("level", level);
//	            b.putString("message", message);
//	            b.putBoolean("test", test);
//	            intent.putExtras(b);
//	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	            TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//	            carrierName = manager.getNetworkOperatorName();
//	            if (carrierName.replaceAll(" ", "").equals(carrier)) {
//
//	                context.startActivity(intent);
//	            } else {
//	                //testing
//	                Toast.makeText(context, carrierName.replaceAll(" ", ""), Toast.LENGTH_LONG).show();
//	            }
//	        }
//	    }
}
