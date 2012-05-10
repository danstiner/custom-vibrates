package com.danielstiner.vibrates.notify;

import android.content.Context;
import android.os.Bundle;

public interface IIntentHandler {

	public abstract void handle(String action, Bundle bundle, Context context);

}