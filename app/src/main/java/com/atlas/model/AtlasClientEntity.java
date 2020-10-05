package com.atlas.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import java.util.LinkedList;
import java.util.List;

public class AtlasClientEntity {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "client_identity")
    private String identity;

    @ColumnInfo(name = "client_commands")
    List<AtlasClientCommandEntity> commands;

    public AtlasClientEntity(Long id, String identity) {
        this.id = id;
        this.identity = identity;
        this.commands = new LinkedList<>();
    }

    public AtlasClientEntity(String identity) {
        this.identity = identity;
        this.commands = new LinkedList<>();
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

    public void addCommand(AtlasClientCommandEntity command) {
        commands.add(command);
    }

}
