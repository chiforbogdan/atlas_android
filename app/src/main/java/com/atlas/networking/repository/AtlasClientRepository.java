package com.atlas.networking.repository;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.atlas.model.database.AtlasClient;
import com.atlas.model.dto.AtlasClientCommandsResp;

import java.util.List;


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

    public MutableLiveData<List<AtlasClient>> getClients(String gatewayIdentity) {
        MutableLiveData<List<AtlasClient>> list = new MutableLiveData<>();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<AtlasClient> clientList = executeClientGetReq(gatewayIdentity);
                list.postValue(clientList);
                return null;
            }
        }.execute();

        return list;
    }

    private List<AtlasClient> executeClientGetReq(String gatewayIdentity) {

        return null;
    }

    private boolean updateAtlasClientCommand(List<AtlasClient> atlasClientEntities, AtlasClientCommandsResp response) {
        return false;
    }
}