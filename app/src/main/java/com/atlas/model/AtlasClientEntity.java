package com.atlas.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class AtlasClientEntity {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "client_identity")
    private String identity;

    public AtlasClientEntity(Long id, String identity) {
        this.id = id;
        this.identity = identity;
    }

    public AtlasClientEntity(String identity) {
        this.identity = identity;
    }

    public Long getId() {
        return id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
