package com.atlas.ui.gateway_claim;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EdgeEffect;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.atlas.database.AtlasDatabase;
import com.atlas.model.AtlasGatewayEntity;
import com.atlas.model.dto.AtlasGatewayClaimReq;
import com.atlas.model.dto.AtlasGatewayClaimResp;
import com.atlas.networking.AtlasGatewayClaimAPI;
import com.atlas.networking.AtlasNetworkAPIFactory;
import com.atlas.utils.AtlasSharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Objects;

import retrofit2.Response;

public class AtlasClaimViewModel extends AndroidViewModel {
    private static final String ATLAS_GATEWAY_HTTPS_SCHEMA = "https://";
    /* Indicates if the gateway alias is valid */
    private MutableLiveData<Boolean> aliasLiveData = new MutableLiveData<>();

    /* Indicates if the gateway was claimed */
    private MutableLiveData<Boolean> claimedLiveData = new MutableLiveData<>();

    public AtlasClaimViewModel(@NonNull Application application) {
        super(application);
    }

    public final MutableLiveData<Boolean> getAliasLiveData() {
        return aliasLiveData;
    }

    public final MutableLiveData<Boolean> getClaimedLiveData() {
        return claimedLiveData;
    }

    public void validateAlias(String alias) {
        Log.d(AtlasClaimViewModel.class.getName(), "Validate alias");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AtlasGatewayEntity gatewayEntity = AtlasDatabase.getInstance(getApplication()
                        .getApplicationContext())
                        .gatewayEntityDao()
                        .selectByAlias(alias);

                int send_alias_data_validity = Log.d(AtlasClaimViewModel.class.getName(), "Send alias data validity");
                aliasLiveData.postValue(true ? gatewayEntity == null : false);

                return null;
            }
        }.execute();

    }

    public void claimGateway(final String ipPort, final String shortCode, final String alias) {
        Log.d(AtlasClaimViewModel.class.getName(), "Start gateway claim process!");

        String ownerID = AtlasSharedPreferences.getInstance(getApplication()).getOwnerID();
        executeClaimReqAsync(ipPort, shortCode, alias, ownerID);
    }

    private void executeClaimReqAsync(final String ipPort, final String shortCode,
                                      final String alias, final String ownerIdentity) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                executeClaimReq(ipPort, shortCode, alias, ownerIdentity);
                return null;
            }
        }.execute();
    }

    private void executeClaimReq(final String ipPort, final String shortCode,
                                 final String alias, final String ownerIdentity) {
        final String url = ATLAS_GATEWAY_HTTPS_SCHEMA + ipPort + "/";
        Log.d(AtlasClaimViewModel.class.getName(), "Execute claim request to URL:" + url);
        try {
            /* Execute REST API request to gateway */
            AtlasGatewayClaimAPI gatewayClaimAPI = AtlasNetworkAPIFactory.createGatewayClaimAPI(url);
            AtlasGatewayClaimReq claimReq = new AtlasGatewayClaimReq(shortCode, "secretKey", ownerIdentity);
            Response<AtlasGatewayClaimResp> claimResp = gatewayClaimAPI.claimGateway(claimReq).execute();
            if (!claimResp.isSuccessful()) {
                Log.e(AtlasClaimViewModel.class.getName(), "Gateway claim REST API is not successful");
                claimedLiveData.postValue(false);
                return;
            }

            /* Insert gateway into database */
            AtlasGatewayEntity gateway = new AtlasGatewayEntity();
            gateway.setIdentity(claimResp.body().getIdentity());
            gateway.setAlias(alias);
            // TODO if gateway exists, secret key should be updated
            AtlasDatabase.getInstance(getApplication().getApplicationContext()).gatewayEntityDao().insertGateway(gateway);

            Log.i(AtlasClaimViewModel.class.getName(),
                    String.format("Gateway claim REST API is successful. Gateway alias is %s and gateway identity is %s", alias, claimResp.body().getIdentity()));
            claimedLiveData.postValue(true);
        } catch (Exception e) {
            Log.e(AtlasClaimViewModel.class.getName(), "Claim request exception: " + e.getMessage());
            claimedLiveData.postValue(false);
            e.printStackTrace();
        }
    }
}
