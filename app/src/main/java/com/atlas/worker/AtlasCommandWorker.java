package com.atlas.worker;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.atlas.BuildConfig;
import com.atlas.database.AtlasDatabase;
import com.atlas.model.database.AtlasClient;
import com.atlas.model.database.AtlasCommand;
import com.atlas.model.database.AtlasGateway;
import com.atlas.model.dto.AtlasClientCommandsResp;
import com.atlas.networking.AtlasClientCommandAPI;
import com.atlas.networking.AtlasNetworkAPIFactory;
import com.atlas.utils.AtlasSharedPreferences;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

import static com.atlas.utils.AtlasConstants.ATLAS_CLIENT_COMMANDS_BROADCAST;

public class AtlasCommandWorker extends Worker {
    public AtlasCommandWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(AtlasCommandWorker.class.getName(), "Execute command worker");

        try {
            String ownerID = AtlasSharedPreferences.getInstance(getApplicationContext()).getOwnerID();
            Log.d(AtlasCommandWorker.class.getName(), "Get commands for owner " + ownerID);

            AtlasClientCommandAPI clientCommandAPI = AtlasNetworkAPIFactory.createClientCommandAPI(BuildConfig.ATLAS_CLOUD_BASE_URL);
            Response<Map<String, List<AtlasClientCommandsResp>>> response = clientCommandAPI
                    .getClientCommands(ownerID)
                    .execute();

            if (!response.isSuccessful()) {
                Log.e(AtlasCommandWorker.class.getName(), "Command list fetch from cloud FAILED!");
                return Result.retry();
            }

            Map<String, List<AtlasClientCommandsResp>> commandList = response.body();
            if (commandList.isEmpty()) {
                return Result.success();
            }

            commandList.keySet().forEach((gateway) -> {
                Log.d(AtlasCommandWorker.class.getName(), "Commands from gateway: " + gateway);

                /* If gateway does not exist locally (it was not claimed) drop the info */
                AtlasGateway gatewayEntity = AtlasDatabase.getInstance(getApplicationContext()).gatewayDao().selectByIdentity(gateway);
                if (gatewayEntity != null) {
                    updateClientCommands(gatewayEntity, commandList.values());
                }
            });

            /* Notify UI about the client/commands change */
            getApplicationContext().sendBroadcast(new Intent(ATLAS_CLIENT_COMMANDS_BROADCAST));

            return Result.success();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.retry();
    }

    private void updateClientCommands(AtlasGateway gateway, Collection<List<AtlasClientCommandsResp>> clientCommands) {
        clientCommands.forEach((commands) -> {
            Log.d(AtlasCommandWorker.class.getName(), "Number of commands is: " + commands.size());
            commands.forEach((command) -> {
                Log.d(AtlasCommandWorker.class.getName(),
                        "Processing command with sequence number " + command.getSeqNo() + " from client " + command.getClientIdentity());

                /* Verify if client exists. If not create client. */
                AtlasClient client = AtlasDatabase.getInstance(getApplicationContext()).clientDao().selectByIdentity(command.getClientIdentity());
                Long clientId;
                if (client == null) {
                    Log.d(AtlasCommandWorker.class.getName(), "Client does not exist. Create it.");
                    client = new AtlasClient();
                    client.setIdentity(command.getClientIdentity());
                    client.setAlias(command.getClientAlias());
                    client.setGatewayId(gateway.getId());
                    clientId = AtlasDatabase.getInstance(getApplicationContext()).clientDao().insertClient(client);
                } else {
                    clientId = client.getId();
                    /* If client alias changed, update it locally */
                    if (!client.getAlias().equalsIgnoreCase(command.getClientAlias())) {
                        client.setAlias(command.getClientAlias());
                        AtlasDatabase.getInstance(getApplicationContext()).clientDao().updateClient(client);
                    }
                }

                /* Verify if command exists. If not create command. */
                AtlasCommand cmd = AtlasDatabase.getInstance(getApplicationContext()).commandDao().selectBySeqNo(command.getSeqNo());
                if (cmd == null) {
                    Log.d(AtlasCommandWorker.class.getName(), "Command does not exist. Create it.");
                    cmd = new AtlasCommand();
                    cmd.setCreateTime(new Date().toString());
                    cmd.setSeqNo(command.getSeqNo());
                    cmd.setType(command.getType());
                    cmd.setClientId(clientId);
                    cmd.setPayload(command.getPayload());

                    AtlasDatabase.getInstance(getApplicationContext()).commandDao().insertCommand(cmd);
                }
            });
        });
    }
}
