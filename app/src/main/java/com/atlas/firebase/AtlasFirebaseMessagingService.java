package com.atlas.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.atlas.utils.AtlasSharedPreferences;
import com.atlas.worker.AtlasFirebaseWorker;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.time.Duration;


public class AtlasFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO handle token
        Log.i(AtlasFirebaseMessagingService.class.getName(), "Firebase notification received!");
    }

    @Override
    public void onNewToken(@NonNull String firebaseToken) {
        Log.d(AtlasFirebaseMessagingService.class.getName(), "Firebase Token refreshed");
        super.onNewToken(firebaseToken);

        AtlasSharedPreferences.getInstance(getApplicationContext()).saveFirebaseToken(firebaseToken);

        /* Notify the cloud that the token has changed */
        OneTimeWorkRequest firebaseUpdateWorker = new OneTimeWorkRequest.Builder(AtlasFirebaseWorker.class)
                .setInitialDelay(Duration.ZERO)
                .setBackoffCriteria(BackoffPolicy.LINEAR, Duration.ofMinutes(1))
                .build();
        WorkManager.getInstance().enqueue(firebaseUpdateWorker);
    }
}
