package com.atlas.gateway;

public class AtlasGateway {

    private String alias;
    private String identity;
    private String psk;
    private int keepAliveCounter;
    private String lastRegisteredTime;
    private String lastKeepAlive;
    private boolean isExpanded;
    private boolean registered;

    public AtlasGateway(String alias, String identity, String psk, int keepAliveCounter, String lastRegisteredTime, String lastKeepAlive) {
        this.alias = alias;
        this.identity = identity;
        this.psk = psk;
        this.keepAliveCounter = keepAliveCounter;
        this.lastRegisteredTime = lastRegisteredTime;
        this.lastKeepAlive = lastKeepAlive;
        this.isExpanded = false;
        this.registered = false;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPsk() {
        return psk;
    }

    public void setPsk(String psk) {
        this.psk = psk;
    }

    public int getKeepAliveCounter() {
        return keepAliveCounter;
    }

    public void setKeepAliveCounter(int keepAliveCounter) {
        this.keepAliveCounter = keepAliveCounter;
    }

    public String getLastRegisteredTime() {
        return lastRegisteredTime;
    }

    public void setLastRegisteredTime(String lastRegisteredTime) {
        this.lastRegisteredTime = lastRegisteredTime;
    }

    public String getLastKeepAlive() {
        return lastKeepAlive;
    }

    public void setLastKeepAlive(String lastKeepAlive) {
        this.lastKeepAlive = lastKeepAlive;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}
