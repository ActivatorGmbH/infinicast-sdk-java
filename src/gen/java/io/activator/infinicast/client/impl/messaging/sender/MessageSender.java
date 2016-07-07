package io.activator.infinicast.client.impl.messaging.sender;
import org.joda.time.DateTime;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.activator.infinicast.client.api.*;
import io.activator.infinicast.client.impl.*;
import io.activator.infinicast.client.utils.*;
import io.activator.infinicast.client.protocol.*;
import io.activator.infinicast.client.api.paths.*;
import io.activator.infinicast.client.api.query.*;
import io.activator.infinicast.client.api.paths.handler.*;
import io.activator.infinicast.client.api.paths.options.*;
import io.activator.infinicast.client.api.paths.taskObjects.*;
import io.activator.infinicast.client.api.paths.handler.messages.*;
import io.activator.infinicast.client.api.paths.handler.reminders.*;
import io.activator.infinicast.client.api.paths.handler.lists.*;
import io.activator.infinicast.client.api.paths.handler.objects.*;
import io.activator.infinicast.client.api.paths.handler.requests.*;
import io.activator.infinicast.client.impl.contexts.*;
import io.activator.infinicast.client.impl.helper.*;
import io.activator.infinicast.client.impl.query.*;
import io.activator.infinicast.client.impl.messaging.*;
import io.activator.infinicast.client.impl.pathAccess.*;
import io.activator.infinicast.client.impl.responder.*;
import io.activator.infinicast.client.impl.objectState.*;
import io.activator.infinicast.client.impl.messaging.receiver.*;
import io.activator.infinicast.client.impl.messaging.handlers.*;
import io.activator.infinicast.client.impl.messaging.sender.*;
import io.activator.infinicast.client.protocol.messages.*;
public class MessageSender implements IMessageSender {
    IEndpoint2ServerNetLayer _connection;
    Logger _logger = LoggerFactory.getLogger(MessageSender.class);
    public MessageSender(IEndpoint2ServerNetLayer connection) {
        this._connection = connection;
    }
    public void sendMessage(Connector2EpsMessage data) {
        APlayStringMessage convertedMsg = data.buildStringMessage();
        if (this._logger.getIsDebugEnabled()) {
            String messageAsString = convertedMsg.getDataAsString();
            this._logger.debug(("Send Message " + messageAsString));
        }
        try {
            this._connection.SendToServer(convertedMsg);
        }
        catch (Exception ex) {
            this._logger.error("Could not send ", ex);
        }
    }
}
