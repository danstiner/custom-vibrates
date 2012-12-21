package com.danielstiner.vibrates.notify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.danielstiner.vibrates.service.VibratrService;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class VibrateNotify {

	private static final String NS = com.danielstiner.vibrates.Vibrates.NS
			+ ".notify";
	private static final String CLASSNAME = NS + ".VibrateNotify";

	private static final String BUNDLE_KEY_EXTRA = CLASSNAME + ".extra";
	private static final String BUNDLE_KEY_IDENTIFIER_ID = CLASSNAME
			+ ".identifier_id";
	private static final String BUNDLE_KEY_IDENTIFIER_TYPE = CLASSNAME
			+ ".identifier_type";
	private static final String BUNDLE_KEY_KIND = CLASSNAME + ".kind";

	public static enum Kind {
		Once, Continuous, Cancel
	}

	private String _identifier;
	private String _type;
	private String _extra;
	private Kind _kind;

	private Provider<Intent> intent_provider;

	@Inject
	public VibrateNotify(Provider<Intent> intent_provider) {
		super();
		this.intent_provider = intent_provider;
		this._kind = Kind.Once;
	}

	public VibrateNotify loadBundle(Bundle bundle) {
		this._identifier = bundle.getString(BUNDLE_KEY_IDENTIFIER_ID);
		this._type = bundle.getString(BUNDLE_KEY_IDENTIFIER_TYPE);
		this._extra = bundle.getString(BUNDLE_KEY_EXTRA);
		this._kind = (Kind) bundle.getSerializable(BUNDLE_KEY_KIND);

		return this;
	}

	public void fire(Context context) {
		Intent i = intent_provider.get();
		i.putExtra(BUNDLE_KEY_IDENTIFIER_ID, this._identifier);
		i.putExtra(BUNDLE_KEY_IDENTIFIER_TYPE, this._type);
		i.putExtra(BUNDLE_KEY_EXTRA, this._extra);
		i.putExtra(BUNDLE_KEY_KIND, this._kind);
		// Tell the vibrator service to get busy
		VibratrService.start(i, context);
	}

	public VibrateNotify extra(String extra) {
		_extra = extra;
		return this;
	}

	public VibrateNotify kind(Kind new_kind) {
		this._kind = new_kind;
		return this;
	}

	public Kind kind() {
		return this._kind;
	}

	public VibrateNotify identifier_id(String identifier) {
		_identifier = identifier;
		return this;
	}

	public String identifier_type() {
		return _type;
	}

	public VibrateNotify identifier_type(String type) {
		_type = type;
		return this;
	}

	public String identifier_id() {
		return _identifier;
	}

	public VibrateNotify identifier(String identifier, String type) {
		_identifier = identifier;
		_type = type;
		return this;
	}

	public boolean sameIdentifier(VibrateNotify other) {
		boolean id, type;
		
		if (other._identifier == null && this._identifier == null)
			id = true;
		else if(other._identifier == null || this._identifier == null)
			id = false;
		else
			id = other._identifier.equals(this._identifier);
		
		if (other._type == null && this._type == null)
			type = true;
		else if(other._type == null || this._type == null)
			type = false;
		else
			type = other._type.equals(this._type);
		
		return id && type;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof VibrateNotify))
			return false;

		VibrateNotify other = (VibrateNotify) o;

		return this.sameIdentifier(other) && other._kind == this._kind;
	}

	public VibrateNotify source_type(String facebookType) {
		// TODO Auto-generated method stub
		return this;
	}
}
