package io.infinicast;

import java.io.Serializable;

public class LowLevelPingMessage implements Serializable, IMessage {
    private long pingTime;
    private int lastRoundTripTime;

    public long getPingTime() {
        return pingTime;
    }

    public void setPingTime(long pingTime) {
        this.pingTime = pingTime;
    }

    public int getLastRoundTripTime() {
        return lastRoundTripTime;
    }

    public void setLastRoundTripTime(int lastRoundTripTime) {
        this.lastRoundTripTime = lastRoundTripTime;
    }
}

