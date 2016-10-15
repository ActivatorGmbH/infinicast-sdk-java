package io.infinicast.client.impl.messaging.sender;

import io.infinicast.client.protocol.messages.Connector2EpsMessage;
public interface IMessageSender {
    void sendMessage(Connector2EpsMessage data);
}
