package com.atlas.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;


public class AtlasFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "AtlasFirebaseMessagingService";

    @Override
    public void onNewToken(@NonNull String s) {
        Log.w(TAG, "Firebase Token refreshed");
        super.onNewToken(s);

        /* ToDo Notify server that client's token changed */
    }
}
