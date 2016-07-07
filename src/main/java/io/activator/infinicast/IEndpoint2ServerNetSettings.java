package io.activator.infinicast;

public interface IEndpoint2ServerNetSettings {
    ServerAddress getServerAddress();
    IEndpoint2ServerNetLayerHandler getHandler(); // should be used to set the cloud handler and write information to it.
    void setHandler(IEndpoint2ServerNetLayerHandler handler);
}

