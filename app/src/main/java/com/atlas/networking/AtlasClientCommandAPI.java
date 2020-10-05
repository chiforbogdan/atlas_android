package com.atlas.networking;

import com.atlas.model.dto.AtlasClientCommandsResp;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AtlasClientCommandAPI {

    @GET("atlas/owner/commands/{owner_identity}")
    Call<Map<String, List<AtlasClientCommandsResp>>> getClientCommands(@Path("owner_identity") String ownerIdentity);
}
