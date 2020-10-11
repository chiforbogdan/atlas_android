package com.atlas.ui.command_list.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atlas.database.AtlasDatabase;
import com.atlas.model.database.AtlasCommand;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AtlasCommandListViewModel extends AndroidViewModel {

    private MutableLiveData<List<AtlasCommand>> commandList;
    private final String clientIdentity;

    public AtlasCommandListViewModel(@NonNull Application application, String clientIdentity) {
        super(application);

        this.clientIdentity = clientIdentity;
        this.commandList = new MutableLiveData<>();

        fetchCommands();
    }

    public void fetchCommands() {
        CompletableFuture.runAsync(() -> {
            Log.d(AtlasCommandListViewModel.class.getName(), "Fetch commands from database");
            commandList.postValue(AtlasDatabase.getInstance(getApplication()).commandDao().selectByClientIdentity(clientIdentity));
        });
    }

    public MutableLiveData<List<AtlasCommand>> getCommandList() {
        return commandList;
    }

    public static class Factory extends ViewModelProvider.AndroidViewModelFactory {

        @NonNull
        Application application;
        private List<AtlasCommand> commandList;
        private String clientIdentity;

        public Factory(@NonNull Application application, String clientIdentity) {
            super(application);
            this.application = application;
            this.commandList = commandList;
            this.clientIdentity = clientIdentity;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AtlasCommandListViewModel(application, clientIdentity);
        }
    }
}
