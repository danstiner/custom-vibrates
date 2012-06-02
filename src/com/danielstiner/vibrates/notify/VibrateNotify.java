package com.danielstiner.vibrates.notify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class VibrateNotify {

	private static final String NS = com.danielstiner.vibrates.Vibrates.NS
			+ ".notify";
	private static final String CLASSNAME = NS + ".VibrateNotify";

	private static final String BUNDLE_KEY_EXTRA = CLASSNAME + ".extra";
	private static final String BUNDLE_KEY_IDENTIFIER = CLASSNAME
			+ ".identifier";
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

	public void loadBundle(Bundle bundle) {
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
