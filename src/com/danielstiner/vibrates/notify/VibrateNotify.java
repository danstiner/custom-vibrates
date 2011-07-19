package com.danielstiner.vibrates.notify;

import com.google.inject.Inject;
import com.google.inject.Provider;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class VibrateNotify extends AbstractHappening {
	
	private static final String CLASSNAME = com.danielstiner.vibrates.Vibrates.NS + ".VibrateNotify";
	
	public static final String BUNDLE_KEY = CLASSNAME;
	
	public static final Parcelable.Creator<VibrateNotify> CREATOR = new Parcelable.Creator<VibrateNotify>()
	{
		public VibrateNotify createFromParcel(Parcel in) {
		    return new VibrateNotify(in);
		}
		
		public VibrateNotify[] newArray(int size) {
		    return new VibrateNotify[size];
		}
	};
	
	private Provider<Intent> intent_provider;
	
	@Inject
	public VibrateNotify(Provider<Intent> intent_provider) {
		super();
		this.intent_provider = intent_provider;
	}

	public VibrateNotify(Parcel in) {
		super(in);
		_extra = in.readString();
	}

	// Very common types, throwing them in here for convenience
	public static final String TYPE_EMAIL     = particularizeType(TYPE_MESSAGE, "chat");
	public static final String TYPE_SMS = particularizeType(TYPE_CHAT, "Status Updates");
	public static final String TYPE_STATUS_UPDATE = particularizeType(TYPE_UPDATE, "Status Updates");
	
	/** List of root types for generating a tree of notification types */
	public static final String[] COMMON_TYPES = {
		TYPE_CHAT,
		TYPE_HARDWARE,
		TYPE_MESSAGE,
		TYPE_UPDATE,
		TYPE_VOICE
		};
	
	private String _extra;

	public VibrateNotify extra(String extra) {
		_extra = extra;
		return this;
	}

	@Inject
	public void fire(Context context) {
		// Tell the vibrator service to get busy
		context.startService(intent_provider.get().setAction(VibratrService.ACTION).putExtra(CLASSNAME, this));
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		
		dest.writeString(_extra);
	}

	public VibrateNotify identifier(String identifier) {
		super.identifier(identifier);
		return this;
	}

	public VibrateNotify type(String type) {
		super.type(type);
		return this;
	}

	
}
