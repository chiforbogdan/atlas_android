package com.atlas.ui.command_list.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atlas.database.AtlasDatabase;
import com.atlas.model.database.AtlasCommand;
import com.atlas.model.dto.AtlasOwnerCommandReq;
import com.atlas.networking.AtlasClientCommandAPI;
import com.atlas.networking.AtlasNetworkAPIFactory;
import com.atlas.utils.AtlasSharedPreferences;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.atlas.utils.AtlasConstants.ATLAS_CLIENT_COMMANDS_BROADCAST;
import static com.atlas.utils.AtlasConstants.ATLAS_CLOUD_BASE_URL;

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

    public boolean approveCommand(AtlasCommand command) throws Exception {
        CompletableFuture<Boolean> asyncApproveTask = new CompletableFuture<Boolean>().supplyAsync(() -> {
            try {
                return sendOwnerCommandRequest(true, command);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).thenApply(result -> {
            return result;
        });
        return asyncApproveTask.get();
    }

    public boolean rejectCommand(AtlasCommand command) throws Exception {
        CompletableFuture<Boolean> asyncApproveTask = new CompletableFuture<Boolean>().supplyAsync(() -> {
            try {
                return sendOwnerCommandRequest(false, command);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }).thenApply(result -> {
            return result;
        });
        return asyncApproveTask.get();
    }

    private boolean sendOwnerCommandRequest(boolean commandStatus, AtlasCommand command) {
        try {
            String gatewayIdentity = AtlasDatabase.getInstance(getApplication()).clientDao().selectGatewayByClientIdentity(clientIdentity).getIdentity();
            String ownerID = AtlasSharedPreferences.getInstance(getApplication()).getOwnerID();

            Log.d(AtlasCommandListViewModel.class.getName(), "Approve command seq.nr. " + String.valueOf(command.getSeqNo()) + " for " + ownerID);

            AtlasOwnerCommandReq ownerCommandReq = new AtlasOwnerCommandReq(gatewayIdentity, clientIdentity, command.getSeqNo().intValue(), commandStatus, "Signature");
            AtlasClientCommandAPI clientCommandAPI = AtlasNetworkAPIFactory.createClientCommandAPI(ATLAS_CLOUD_BASE_URL);
            Response<ResponseBody> response = clientCommandAPI.sendCommandStatus(ownerID, ownerCommandReq).execute();

            if (!response.isSuccessful()) {
                Log.e(AtlasCommandListViewModel.class.getName(), "Owner command update FAILED!");
                return false;
            }

            /* Delete command from database */
            AtlasDatabase.getInstance(getApplication()).commandDao().deleteCommand(command);
            Log.d(AtlasCommandListViewModel.class.getName(), "Command has been DELETED!");

            /* Notify UI about the client/commands change */
            getApplication().sendBroadcast(new Intent(ATLAS_CLIENT_COMMANDS_BROADCAST));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class Factory extends ViewModelProvider.AndroidViewModelFactory {

        @NonNull
        Application application;
        private List<AtlasCommand> commandList;
        private String clientIdentity;

        public Factory(@NonNull Application application, String clientIdentity) {
            super(application);
            this.application = application;
            this.clientIdentity = clientIdentity;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new AtlasCommandListViewModel(application, clientIdentity);
        }
    }
}
