package com.atlas.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"gateway_identity", "gateway_alias"},
        unique = true)})
public class AtlasGatewayEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "gateway_identity")
    private String identity;

    @ColumnInfo(name = "gateway_alias")
    private String alias;

    public AtlasGatewayEntity() {}

    public AtlasGatewayEntity(Long id, String identity, String alias) {
        this.id = id;
        this.identity = identity;
        this.alias = alias;
    }

    public Long getId() {
        return id;
    }

    public String getIdentity() {
        return identity;
    }

    public String getAlias() {
        return alias;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}