package io.infinicast.client.utils;

import io.infinicast.APlayStringMessage;
import io.infinicast.ServerAddress;
public class NetFactory {
    public static APlayStringMessage createMessage() {
        return new APlayStringMessage();
    }
    public static ServerAddress createServerAddress(String address) {
        ServerAddress result = new ServerAddress();
        result.setAddress(address);
        return result;
    }
}
