package com.atlas.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class AtlasClientCommandEntity {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "command_type")
    private AtlasClientCommandType type;

    @ColumnInfo(name = "command_payload")
    private String payload;

    @ColumnInfo(name = "sequence_number")
    private int seqNo;

    @ColumnInfo(name = "command_signature")
    private String signature;

    public AtlasClientCommandEntity(AtlasClientCommandType type, String payload, int seqNo, String signature) {
        this.type = type;
        this.payload = payload;
        this.seqNo = seqNo;
        this.signature = signature;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AtlasClientCommandType getType() {
        return type;
    }

    public void setType(AtlasClientCommandType type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
