package com.atlas.ui.gateway_list.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.atlas.database.AtlasDatabase;
import com.atlas.model.database.AtlasGateway;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AtlasGatewayListViewModel extends AndroidViewModel {
    private final MutableLiveData<List<AtlasGateway>> gatewayList = new MutableLiveData<>();

    public AtlasGatewayListViewModel(@NonNull Application application) {
        super(application);
        fetchGatewayList();
    }

    public void fetchGatewayList() {
        CompletableFuture.runAsync(() -> {
            List<AtlasGateway> gateways = AtlasDatabase.getInstance(getApplication()
                    .getApplicationContext())
                    .gatewayDao()
                    .selectAll();

            gateways.forEach((gateway) -> {
                gateway.setPendingCommands(AtlasDatabase.getInstance(getApplication()
                        .getApplicationContext())
                        .gatewayDao()
                        .getPendingCommands(gateway.getIdentity()));
            });

            gatewayList.postValue(gateways);
        });
    }

    public MutableLiveData<List<AtlasGateway>> getGatewayList() {
        return gatewayList;
    }
}
