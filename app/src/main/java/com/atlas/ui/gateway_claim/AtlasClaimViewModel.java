package com.atlas.ui.gateway_claim;

import android.app.Application;
import android.util.Log;

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

import java.util.concurrent.CompletableFuture;

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

        CompletableFuture.runAsync(() -> {
            AtlasGatewayEntity gatewayEntity = AtlasDatabase.getInstance(getApplication()
                    .getApplicationContext())
                    .gatewayEntityDao()
                    .selectByAlias(alias);

            Log.d(AtlasClaimViewModel.class.getName(), "Send alias data validity");
            aliasLiveData.postValue(true ? gatewayEntity == null : false);
        });
    }

    public void claimGateway(final String ipPort, final String shortCode, final String alias) {
        Log.d(AtlasClaimViewModel.class.getName(), "Start gateway claim process!");

        new CompletableFuture<Boolean>().supplyAsync(() -> {
            String ownerID = AtlasSharedPreferences.getInstance(getApplication()).getOwnerID();
            final String url = ATLAS_GATEWAY_HTTPS_SCHEMA + ipPort + "/";
            Log.d(AtlasClaimViewModel.class.getName(), "Execute claim request to URL:" + url);

            try {
                /* Execute REST API request to gateway */
                AtlasGatewayClaimAPI gatewayClaimAPI = AtlasNetworkAPIFactory.createGatewayClaimAPI(url);
                AtlasGatewayClaimReq claimReq = new AtlasGatewayClaimReq(shortCode, "secretKey", ownerID);
                Response<AtlasGatewayClaimResp> claimResp = gatewayClaimAPI.claimGateway(claimReq).execute();
                if (!claimResp.isSuccessful()) {
                    Log.e(AtlasClaimViewModel.class.getName(), "Gateway claim REST API is not successful");
                    return false;
                }

                /* Insert gateway into database */
                AtlasGatewayEntity gateway = new AtlasGatewayEntity();
                gateway.setIdentity(claimResp.body().getIdentity());
                gateway.setAlias(alias);
                // TODO if gateway exists, secret key should be updated
                AtlasDatabase.getInstance(getApplication().getApplicationContext()).gatewayEntityDao().insertGateway(gateway);

                Log.i(AtlasClaimViewModel.class.getName(),
                        String.format("Gateway claim REST API is successful. Gateway alias is %s and gateway identity is %s", alias, claimResp.body().getIdentity()));

                return true;
            } catch (Exception e) {
                Log.e(AtlasClaimViewModel.class.getName(), "Claim request exception: " + e.getMessage());
                e.printStackTrace();
            }

            return false;
        }).thenAccept(claimStatus -> claimedLiveData.postValue(claimStatus));
    }
}
