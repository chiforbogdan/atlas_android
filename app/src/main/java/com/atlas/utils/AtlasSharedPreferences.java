package com.atlas.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AtlasSharedPreferences {
    private static AtlasSharedPreferences instance;
    private static Object lock = new Object();
    private SharedPreferences sharedPref;
    private Application app;

    public static AtlasSharedPreferences getInstance(Application app) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new AtlasSharedPreferences(app);
                }
            }
        }

        return instance;
    }

    public String getOwnerID() {
        return sharedPref.getString(AtlasConstants.ATLAS_SHARED_PREF_OWNER_ID, null);
    }

    public void saveOwnerID(String ownerID) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AtlasConstants.ATLAS_SHARED_PREF_OWNER_ID, ownerID);
        editor.commit();
    }

    private AtlasSharedPreferences(Application app) {
        sharedPref = app.getSharedPreferences(AtlasConstants.ATLAS_SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }
}
