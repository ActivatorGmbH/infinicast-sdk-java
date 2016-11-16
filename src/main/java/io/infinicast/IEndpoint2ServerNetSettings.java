package io.infinicast;

public interface IEndpoint2ServerNetSettings {
    InfinicastServerAddress getServerAddress();
    IEndpoint2ServerNetLayerHandler getHandler(); // should be used to set the cloud handler and write information to it.
    void setHandler(IEndpoint2ServerNetLayerHandler handler);
}

