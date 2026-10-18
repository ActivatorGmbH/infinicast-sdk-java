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
        sendMessageInternal(data, false);
    }
    /** Response sends need an observable transport failure to release their handler. */
    public void sendMessageChecked(Connector2EpsMessage data) {
        sendMessageInternal(data, true);
    }
    private void sendMessageInternal(Connector2EpsMessage data, boolean checked) {
        APlayStringMessage convertedMsg = data.buildStringMessage();
        // Connect messages can carry credentials and service secrets.
        // Never log the serialized protocol payload, even at debug level.
        this._logger.debug("Send Message");
        try {
            this._connection.SendToServer(convertedMsg);
        }
        catch (Exception ex) {
            // A transport exception can echo the message it failed to send.
            this._logger.error("Could not send: " + ex.getClass().getSimpleName());
            if (checked) throw new IllegalStateException("transport_send_failed");
        }
    }
    public void destroy() {
    }
}
