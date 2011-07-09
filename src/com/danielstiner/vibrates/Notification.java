package com.danielstiner.vibrates;

import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class Notification {
	private static final String PREFIX = "com.danielstiner.vibrates.notification";

	public static final String IDENTIFIER_KEY = PREFIX + ".identifier";
	public static final String TYPE_KEY = PREFIX + ".type";
	public static final String EXTRA_KEY = PREFIX + ".extra";
	
	public static final String HARDWARE = "hardware";
	public static final String MESSAGE = "messages";
	public static final String STATUS = "status_updates";
	public static final String VOICE = "voice";
	
	// Very common types, throwing them in here for convenience
	public static final String EMAIL = particularizeType(MESSAGE, "email");
	public static final String SMS = particularizeType(MESSAGE, "sms");
	
	// Combination of all known types for enumerating
	public static final String[] COMMON_TYPES = {
		HARDWARE, MESSAGE, STATUS, VOICE, EMAIL, SMS
	};

	public static final String PATTERN_KEY = null;

	public static final String DEFAULT = null;
	
	public static String particularizeType(String type, String specifier) {
		return type + "/" + specifier;
	}
	
	
	private String _identifier;
	private String _type;
	private String _extra;

	public Notification(String identifier) {
		this(identifier, null);
	}
	public Notification(String identifier, String type) {
		// TODO Auto-generated constructor stub
		_identifier = identifier;
		_type = type;
	}
	
	public Notification setType(String new_type) {
		_type = new_type;
		return this;
	}
	public Notification setExtra(String extra) {
		_extra = extra;
		return this;
	}

	public void fire(Context context) {
		// Tell the vibrator service to get busy
		Intent service = new Intent(VibratrService.ACTION);
		service.putExtra(Notification.IDENTIFIER_KEY, _identifier);
		service.putExtra(Notification.TYPE_KEY, _type);
		service.putExtra(Notification.EXTRA_KEY, _extra);
		context.startService(service);
	}

	
}
