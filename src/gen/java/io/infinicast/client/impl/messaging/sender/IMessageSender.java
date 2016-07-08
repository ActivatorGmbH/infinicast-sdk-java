package io.infinicast.client.impl.messaging.sender;
import io.infinicast.client.protocol.messages.Connector2EpsMessage;
import io.infinicast.*;

public interface IMessageSender {
    void sendMessage(Connector2EpsMessage data);
}
