package com.atlas.model.dto;

public class AtlasClientCommandsResp {

    /* Client identity */
    private String clientIdentity;

    /* Command type */
    private String type;

    /* Command payload */
    private String payload;

    /* Command sequence number */
    private int seqNo;

    /* Command owner signature */
    private String signature;

    public AtlasClientCommandsResp(String clientIdentity, String type, String payload, int seqNo, String signature) {
        this.clientIdentity = clientIdentity;
        this.type = type;
        this.payload = payload;
        this.seqNo = seqNo;
        this.signature = signature;
    }

    public String getClientIdentity() {
        return clientIdentity;
    }

    public void setClientIdentity(String clientIdentity) {
        this.clientIdentity = clientIdentity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
