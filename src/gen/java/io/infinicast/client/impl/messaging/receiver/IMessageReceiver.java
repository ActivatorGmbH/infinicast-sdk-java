package io.infinicast.client.impl.messaging.receiver;

import io.infinicast.APlayStringMessage;
import io.infinicast.client.api.IPath;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;
import io.infinicast.client.protocol.Connector2EpsMessageType;
public interface IMessageReceiver {
    void addHandler(String messageType, IPath path, DCloudMessageHandler handler);
    void addResponseHandler(Connector2EpsMessageType messageType, String requestId, DCloudMessageHandler handler);
    void receive(APlayStringMessage stringMessage);
    void setConnector(IConnector connector);
    void removeHandlers(String messageType, String path);
    void destroy();
}
