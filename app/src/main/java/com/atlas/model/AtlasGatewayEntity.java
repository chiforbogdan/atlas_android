package com.atlas.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AtlasGatewayEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "gateway_identity")
    private String identity;

    @ColumnInfo(name = "gateway_friendly_name")
    private String friendlyName;

    public AtlasGatewayEntity(Long id, String identity, String friendlyName) {
        this.id = id;
        this.identity = identity;
        this.friendlyName = friendlyName;
    }

    public Long getId() {
        return id;
    }

    public String getIdentity() {
        return identity;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
