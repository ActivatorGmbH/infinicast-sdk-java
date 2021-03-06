package io.infinicast.client.impl.messaging.sender;

import io.infinicast.APlayStringMessage;
import io.infinicast.IEndpoint2ServerNetLayer;
import io.infinicast.client.protocol.messages.Connector2EpsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSender implements IMessageSender {
    IEndpoint2ServerNetLayer _connection;
    private static Logger _logger = LoggerFactory.getLogger(MessageSender.class);
    public MessageSender(IEndpoint2ServerNetLayer connection) {
        this._connection = connection;
    }
    public void sendMessage(Connector2EpsMessage data) {
        APlayStringMessage convertedMsg = data.buildStringMessage();
        if (this._logger.isDebugEnabled()) {
            String messageAsString = convertedMsg.getDataAsString();
            this._logger.debug("Send Message " + messageAsString);
        }
        try {
            this._connection.SendToServer(convertedMsg);
        }
        catch (Exception ex) {
            this._logger.error("Could not send ", ex);
        }
    }
    public void destroy() {
    }
}
