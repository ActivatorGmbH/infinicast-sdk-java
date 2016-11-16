package io.infinicast;

public class Endpoint2ServerNetSettings implements IEndpoint2ServerNetSettings {
    InfinicastServerAddress address;
    IEndpoint2ServerNetLayerHandler handler;
    @Override
    public InfinicastServerAddress getServerAddress() {
        return address;
    }
    public void setServerAddress(InfinicastServerAddress address) {
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

