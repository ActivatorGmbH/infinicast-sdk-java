package io.activator.infinicast;

public interface IEndpoint2ServerNetLayerHandler {
    void onReceiveFromServer(APlayStringMessage message);
    void onConnect();
    void onDisconnect();
}

