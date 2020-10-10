package com.atlas.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class AtlasClientCommandEntity implements Parcelable {
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

    protected AtlasClientCommandEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        payload = in.readString();
        seqNo = in.readInt();
        signature = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(payload);
        dest.writeInt(seqNo);
        dest.writeString(signature);
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

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AtlasClientCommandEntity> CREATOR = new Creator<AtlasClientCommandEntity>() {
        @Override
        public AtlasClientCommandEntity createFromParcel(Parcel in) {
            return new AtlasClientCommandEntity(in);
        }

        @Override
        public AtlasClientCommandEntity[] newArray(int size) {
            return new AtlasClientCommandEntity[size];
        }
    };


}
