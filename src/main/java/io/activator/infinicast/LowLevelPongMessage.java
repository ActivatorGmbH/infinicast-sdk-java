package io.activator.infinicast;

import java.io.Serializable;

public class LowLevelPongMessage implements Serializable,IMessage {
    private long pingTime;

    public long getPingTime() {
        return pingTime;
    }

    public void setPingTime(long pingTime) {
        this.pingTime = pingTime;
    }
}

