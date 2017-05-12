package io.infinicast.client.utils;

import io.infinicast.APlayStringMessage;
import io.infinicast.InfinicastServerAddress;
public class NetFactory {
    public static APlayStringMessage createMessage() {
        return new APlayStringMessage();
    }
    public static InfinicastServerAddress createServerAddress(String address) {
        InfinicastServerAddress result = new InfinicastServerAddress();
        result.setAddress(address);
        return result;
    }
}
