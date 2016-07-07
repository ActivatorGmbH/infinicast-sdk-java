package io.infinicast;

public class Endpoint2ServerNetSettings implements IEndpoint2ServerNetSettings {
    ServerAddress address;
    IEndpoint2ServerNetLayerHandler handler;
    @Override
    public ServerAddress getServerAddress() {
        return address;
    }
    public void setServerAddress(ServerAddress address) {
        this.address = address;
    }
    @Override
    public IEndpoint2ServerNetLayerHandler getHandler() {
        return handler;
    }
    public void setHandler(IEndpoint2ServerNetLayerHandler handler) {
        this.handler = handler;
    }
}

