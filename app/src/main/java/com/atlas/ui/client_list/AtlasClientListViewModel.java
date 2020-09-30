package com.atlas.ui.client_list;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atlas.model.AtlasClientEntity;

import java.util.ArrayList;
import java.util.List;

public class AtlasClientListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<AtlasClientEntity>> clientList = new MutableLiveData<>();
    private final String gatewayIdentity;

    public AtlasClientListViewModel(@NonNull Application application, String gatewayIdentity) {
        super(application);

        /*ToDO Database client list fetch */
        List<AtlasClientEntity> list = new ArrayList<AtlasClientEntity>();
        list.add(new AtlasClientEntity((long) 1, "client1"));
        list.add(new AtlasClientEntity((long) 3, "client2"));
        list.add(new AtlasClientEntity((long) 2, "client3"));

        this.clientList.setValue(list);
        this.gatewayIdentity = gatewayIdentity;

        Log.w(this.getClass().toString(), gatewayIdentity);
    }

    public MutableLiveData<List<AtlasClientEntity>> getClientList() {
        return clientList;
    }

    public static class Factory extends ViewModelProvider.AndroidViewModelFactory {

        @NonNull
        Application application;

        private final String gatewayIdentity;

        public Factory(@NonNull Application application, String gatewayIdentity) {
            super(application);
            this.application = application;
            this.gatewayIdentity = gatewayIdentity;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AtlasClientListViewModel(application, gatewayIdentity);
        }
    }
}
