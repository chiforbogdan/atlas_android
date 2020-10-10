package com.atlas.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class AtlasFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO handle token
    }

    @Override
    public void onNewToken(@NonNull String firebaseToken) {
        Log.d(AtlasFirebaseMessagingService.class.getName(), "Firebase Token refreshed");
        super.onNewToken(firebaseToken);

        /* Notify the cloud that the token has changed */
        AtlasFirebaseUtils.updateFirebaseTokenToCloud(getApplicationContext(), firebaseToken);
    }
}
