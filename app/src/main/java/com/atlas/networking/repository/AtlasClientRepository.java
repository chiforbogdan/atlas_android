package com.atlas.networking.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.atlas.model.AtlasClientCommandEntity;
import com.atlas.model.AtlasClientCommandType;
import com.atlas.model.AtlasClientEntity;
import com.atlas.model.dto.AtlasClientCommandsResp;
import com.atlas.networking.AtlasClientCommandAPI;
import com.atlas.networking.AtlasNetworkAPIFactory;
import com.atlas.utils.AtlasSharedPreferences;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;


public class AtlasClientRepository {

    private String ATLAS_CLOUD_BASE_URL = "http://10.20.0.37:10000/";

    private static AtlasClientRepository atlasClientRepository;

    private AtlasClientRepository() {
    }

    public static AtlasClientRepository getInstance() {
        if (atlasClientRepository == null) {
            atlasClientRepository = new AtlasClientRepository();
        }

        return atlasClientRepository;
    }

    public MutableLiveData<List<AtlasClientEntity>> getClients(String gatewayIdentity) {
        MutableLiveData<List<AtlasClientEntity>> list = new MutableLiveData<>();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<AtlasClientEntity> clientList = executeClientGetReq(gatewayIdentity);
                list.postValue(clientList);
                return null;
            }
        }.execute();

        return list;
    }

    private List<AtlasClientEntity> executeClientGetReq(String gatewayIdentity) {
        try {
            String owner = null;
            Log.w(AtlasClientRepository.class.getName(), "Get clients from cloud for owner " + owner);
            AtlasClientCommandAPI clientCommandAPI = AtlasNetworkAPIFactory.createClientCommandAPI(ATLAS_CLOUD_BASE_URL);

            Response<Map<String, List<AtlasClientCommandsResp>>> clientCommandsResp = clientCommandAPI
                    .getClientCommands(owner)
                    .execute();

            if (!clientCommandsResp.isSuccessful()) {
                Log.e(AtlasClientRepository.class.getName(), "Client list fetch from cloud FAILED!");
                return null;
            }

            if (clientCommandsResp.body().get(gatewayIdentity) == null) {
                Log.w(AtlasClientRepository.class.getName(), "Client list is null!");
                return null;
            }

            List<AtlasClientCommandsResp> responseList = clientCommandsResp.body().get(gatewayIdentity);
            List<AtlasClientEntity> atlasClientEntities = new LinkedList<>();

            for (AtlasClientCommandsResp resp : responseList) {
                /* Check if client is already defined */
                if (updateAtlasClientCommand(atlasClientEntities, resp))
                    continue;

                AtlasClientEntity atlasClient = new AtlasClientEntity(resp.getClientIdentity());
                atlasClient.addCommand(new AtlasClientCommandEntity(new AtlasClientCommandType(resp.getType()), resp.getPayload(), resp.getSeqNo(), resp.getSignature()));
                atlasClientEntities.add(atlasClient);
            }

            return atlasClientEntities;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean updateAtlasClientCommand(List<AtlasClientEntity> atlasClientEntities, AtlasClientCommandsResp response) {

        for (AtlasClientEntity clientEntity : atlasClientEntities) {
            if (clientEntity.getIdentity().equals(response.getClientIdentity())) {
                clientEntity.addCommand(new AtlasClientCommandEntity(new AtlasClientCommandType(response.getType()), response.getPayload(), response.getSeqNo(), response.getSignature()));
                return true;
            }
        }
        return false;
    }
}