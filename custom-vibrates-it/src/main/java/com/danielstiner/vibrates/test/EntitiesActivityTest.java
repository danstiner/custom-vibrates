package com.danielstiner.vibrates.test;

import android.test.ActivityInstrumentationTestCase2;
import com.danielstiner.vibrates.*;
import com.danielstiner.vibrates.view.EntitiesActivity;

public class EntitiesActivityTest extends ActivityInstrumentationTestCase2<EntitiesActivity> {

    public EntitiesActivityTest() {
        super(EntitiesActivity.class); 
    }

    public void testActivity() {
    	EntitiesActivity activity = getActivity();
        assertNotNull(activity);
    }
}

