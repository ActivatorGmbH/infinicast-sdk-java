package io.infinicast.client.impl.messaging.sender;
import org.joda.time.DateTime;
import io.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
