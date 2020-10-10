package com.atlas.firebase;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.atlas.model.dto.AtlasOwnerFirebase;
import com.atlas.networking.AtlasFirebaseAPI;
import com.atlas.networking.AtlasNetworkAPIFactory;
import com.atlas.ui.main.MainActivity;
import com.atlas.utils.AtlasSharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.jetbrains.annotations.NotNull;

import needle.Needle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.atlas.utils.AtlasConstants.ATLAS_CLOUD_BASE_URL;

public class AtlasFirebaseUtils {
    public static void updateFirebaseTokenToCloud(@NotNull Context context, @Nullable String firebaseToken) {
        if (firebaseToken == null) {
            /* Upload firebase token to cloud */
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                    new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (task.isSuccessful()) {
                                Log.i(AtlasFirebaseUtils.class.getName(), "Firebase token obtained");
                                sendFirebaseTokenToCloud(context, task.getResult().getToken().toString());
                            } else {
                                Log.e(AtlasFirebaseUtils.class.getName(), "Firebase token error", task.getException());
                            }
                        }
                    });
        } else {
            sendFirebaseTokenToCloud(context, firebaseToken);
        }
    }

    private static void sendFirebaseTokenToCloud(Context context, String firebaseToken) {
        Log.d(AtlasFirebaseUtils.class.getName(), "Send Firebase token to cloud: " + firebaseToken);

        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(AtlasFirebaseUtils.class.getName(), "Execute firebase update async task");

                try {
                    String ownerID = AtlasSharedPreferences.getInstance(context).getOwnerID();
                    AtlasOwnerFirebase ownerFirebase = new AtlasOwnerFirebase(firebaseToken);
                    /* Update firebase token to cloud service */
                    AtlasFirebaseAPI firebaseAPI = AtlasNetworkAPIFactory.createFirebaseAPI(ATLAS_CLOUD_BASE_URL);
                    firebaseAPI.updateFirebaseToken(ownerID, ownerFirebase).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.i(AtlasFirebaseUtils.class.getName(), "Firebase token updated successfully to cloud");
                            } else {
                                Log.e(AtlasFirebaseUtils.class.getName(), "An error occurred while updating the firebase token to cloud. HTTP status: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e(AtlasFirebaseUtils.class.getName(), "An error occurred while updating the firebase token to cloud");
                        }
                    });
                } catch (Exception e) {
                    Log.e(AtlasFirebaseUtils.class.getName(), "Exception occurred when updating the firebase token: " + e.getMessage());
                }
            }
        });
    }
}
