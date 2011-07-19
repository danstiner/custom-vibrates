package com.danielstiner.vibrates.notify;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class AbstractHappening implements Parcelable {
	
	public static String particularizeType(String type, String specifier) {
		return type + "/" + specifier;
	}
	
	public static final String TYPE_DEFAULT  = "";
	public static final String TYPE_CHAT     = particularizeType(TYPE_DEFAULT, "Chat");
	public static final String TYPE_HARDWARE = particularizeType(TYPE_DEFAULT, "Hardware");
	public static final String TYPE_MESSAGE  = particularizeType(TYPE_DEFAULT, "Messaging");
	public static final String TYPE_UPDATE   = particularizeType(TYPE_DEFAULT, "Updates");
	public static final String TYPE_VOICE    = particularizeType(TYPE_DEFAULT, "Voice");
	

	private String _identifier;
	private String _type;
	
	public AbstractHappening() {
		this._identifier = null;
		this._type = null;
	}
	
	public AbstractHappening(Parcel in) {
		this._identifier = in.readString();
		this._type = in.readString();
	}
	
	public String identifier() {
		return _identifier;
	}
	
	public AbstractHappening identifier(String identifier) {
		_identifier = identifier;
		return this;
	}

	public String type() {
		return _type;
	}

	public AbstractHappening type(String type) {
		_type = type;
		return this;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(_identifier);
		dest.writeString(_type);
	}

}
