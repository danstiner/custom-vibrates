package com.danielstiner.vibrates.notify;

import com.google.inject.Inject;
import com.google.inject.Provider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class VibrateNotify {
	
	private static final String NS = com.danielstiner.vibrates.Vibrates.NS + ".notify";
	private static final String CLASSNAME = NS + ".VibrateNotify";
	
	public static String particularizeType(String type, String specifier) {
		return type + "/" + specifier;
	}
	
	public static final String TYPE_DEFAULT  = "";
	public static final String TYPE_CHAT     = particularizeType(TYPE_DEFAULT, "Chat");
	public static final String TYPE_HARDWARE = particularizeType(TYPE_DEFAULT, "Hardware");
	public static final String TYPE_MESSAGE  = particularizeType(TYPE_DEFAULT, "Messaging");
	public static final String TYPE_UPDATE   = particularizeType(TYPE_DEFAULT, "Updates");
	public static final String TYPE_VOICE    = particularizeType(TYPE_DEFAULT, "Voice");
	
	// Very common types, throwing them in here for convenience
	public static final String TYPE_EMAIL     = particularizeType(TYPE_MESSAGE, "email");
	public static final String TYPE_SMS       = particularizeType(TYPE_CHAT, "Status Updates");
	public static final String TYPE_STATUS_UPDATE = particularizeType(TYPE_UPDATE, "Status Updates");
	
	/** List of root types for generating a tree of notification types */
	public static final String[] COMMON_TYPES = {
		TYPE_CHAT,
		TYPE_HARDWARE,
		TYPE_MESSAGE,
		TYPE_UPDATE,
		TYPE_VOICE
		};
	
	private static final String BUNDLE_KEY_EXTRA = CLASSNAME + ".extra";
	private static final String BUNDLE_KEY_IDENTIFIER = CLASSNAME + ".identifier";
	private static final String BUNDLE_KEY_TYPE = CLASSNAME + ".type";	
	
	private String _identifier;
	private String _type;
	private String _extra;
	
	private Provider<Intent> intent_provider;
	
	@Inject
	public VibrateNotify(Provider<Intent> intent_provider) {
		super();
		this.intent_provider = intent_provider;
	}
	
	public void loadBundle(Bundle bundle)
	{
		this._identifier = bundle.getString(BUNDLE_KEY_IDENTIFIER);
		this._type = bundle.getString(BUNDLE_KEY_TYPE);
		this._extra = bundle.getString(BUNDLE_KEY_EXTRA);
	}
	
	public void fire(Context context) {
		Intent i = intent_provider.get();
		i.setAction(VibratrService.ACTION);
		i.putExtra(BUNDLE_KEY_IDENTIFIER, this._identifier);
		i.putExtra(BUNDLE_KEY_TYPE, this._type);
		i.putExtra(BUNDLE_KEY_EXTRA, this._extra);
		// Tell the vibrator service to get busy
		context.startService(i);
	}

	public VibrateNotify extra(String extra) {
		_extra = extra;
		return this;
	}

	public String identifier() {
		return _identifier;
	}
	
	public VibrateNotify identifier(String identifier) {
		_identifier = identifier;
		return this;
	}

	public String type() {
		return _type;
	}

	public VibrateNotify type(String type) {
		_type = type;
		return this;
	}
}
