package com.atlas.ui.command_list.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atlas.model.AtlasClientCommandEntity;

import java.util.List;

public class AtlasCommandListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<AtlasClientCommandEntity>> commandList;
    private final String clientIdentity;

    public AtlasCommandListViewModel(@NonNull Application application, String clientIdentity, List<AtlasClientCommandEntity> commandList) {
        super(application);

        this.commandList = new MutableLiveData<>(commandList);
        this.clientIdentity = clientIdentity;
    }

    public MutableLiveData<List<AtlasClientCommandEntity>> getCommandList() {
        return commandList;
    }

    public static class Factory extends ViewModelProvider.AndroidViewModelFactory {

        @NonNull
        Application application;
        private List<AtlasClientCommandEntity> commandList;
        private String clientIdentity;

        public Factory(@NonNull Application application, String clientIdentity, List<AtlasClientCommandEntity> commandList) {
            super(application);
            this.application = application;
            this.commandList = commandList;
            this.clientIdentity = clientIdentity;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AtlasCommandListViewModel(application, clientIdentity, commandList);
        }
    }
}
