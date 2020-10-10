package com.atlas.ui.client_list.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atlas.model.AtlasClientEntity;
import com.atlas.networking.repository.AtlasClientRepository;

import java.util.List;

public class AtlasClientListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<AtlasClientEntity>> clientList;
    private final String gatewayIdentity;

    public AtlasClientListViewModel(@NonNull Application application, String gatewayIdentity) {
        super(application);

        clientList = AtlasClientRepository.getInstance().getClients(gatewayIdentity);
        this.gatewayIdentity = gatewayIdentity;
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
