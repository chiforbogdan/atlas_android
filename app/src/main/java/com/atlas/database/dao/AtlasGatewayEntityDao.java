package com.atlas.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.atlas.gateway.AtlasGateway;
import com.atlas.model.AtlasGatewayEntity;

import java.util.List;

@Dao
public interface AtlasGatewayEntityDao {
    @Query("SELECT * FROM AtlasGatewayEntity")
    List<AtlasGatewayEntity> selectAll();

    @Query("SELECT * FROM AtlasGatewayEntity WHERE gateway_alias = :aliasValue")
    AtlasGatewayEntity selectByAlias(String aliasValue);

    @Insert
    void insertGateway(AtlasGatewayEntity gateway);

    @Update
    void updateGateway(AtlasGatewayEntity gateway);

    @Delete
    void deleteGateway(AtlasGatewayEntity gateway);

    @Query("DELETE FROM AtlasGatewayEntity")
    void deleteAll();
}
