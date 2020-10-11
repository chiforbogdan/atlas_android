package com.atlas.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.atlas.model.database.AtlasClient;

import java.util.List;

@Dao
public interface AtlasClientDao {
    @Query("SELECT * FROM AtlasClient")
    List<AtlasClient> selectAll();

    @Query("SELECT * FROM AtlasClient WHERE client_identity = :identity")
    AtlasClient selectByIdentity(String identity);

    @Query("SELECT * FROM AtlasClient INNER JOIN AtlasGateway ON AtlasClient.client_gateway_id = AtlasGateway.gateway_id WHERE AtlasGateway.gateway_identity = :gatewayIdentity")
    List<AtlasClient> selectByGatewayIdentity(String gatewayIdentity);

    @Insert
    Long insertClient(AtlasClient client);

    @Update
    void updateClient(AtlasClient client);

    @Delete
    void deleteClient(AtlasClient client);

    @Query("DELETE FROM AtlasClient")
    void deleteAll();
}
