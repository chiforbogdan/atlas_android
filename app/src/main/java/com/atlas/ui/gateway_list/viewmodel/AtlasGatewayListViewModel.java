package com.atlas.ui.gateway_list.viewmodel;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.atlas.database.AtlasDatabase;
import com.atlas.model.database.AtlasGateway;

import java.util.List;

public class AtlasGatewayListViewModel extends AndroidViewModel {
    private final MutableLiveData<List<AtlasGateway>> gatewayList = new MutableLiveData<>();

    public AtlasGatewayListViewModel(@NonNull Application application) {
        super(application);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                List<AtlasGateway> data = AtlasDatabase.getInstance(getApplication()
                        .getApplicationContext())
                        .gatewayDao()
                        .selectAll();

                gatewayList.postValue(data);

                return null;
            }
        }.execute();
    }

    public MutableLiveData<List<AtlasGateway>> getGatewayList() {
        return gatewayList;
    }
}
