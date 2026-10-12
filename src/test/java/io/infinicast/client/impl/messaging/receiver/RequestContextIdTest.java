package io.infinicast.client.impl.messaging.receiver;

import io.infinicast.JObject;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestContextIdTest {
    private static final class CapturingReceiver extends ConnectorMessageReceiver {
        IPathAndEndpointContext context;

        CapturingReceiver() {
            super(mock(ConnectorMessageManager.class));
        }

        @Override
        boolean callHandlers(ICError error, String path, Connector2EpsMessageType type,
                             JObject data, IPathAndEndpointContext requestContext, int requestId) {
            context = requestContext;
            return true;
        }
    }

    @Test
    public void receivedRequestCarriesCoreRequestIdToServiceContext() {
        CapturingReceiver receiver = new CapturingReceiver();
        IConnector connector = mock(IConnector.class);
        PathImpl path = new PathImpl("/acp/ban/");
        when(connector.getRootPath()).thenReturn(new PathImpl("/"));
        when(connector.path("/acp/ban/")).thenReturn(path);
        receiver.setConnector(connector);

        JObject sender = new JObject();
        sender.set("path", "/");
        sender.set("endpoint", "test-endpoint");
        receiver.onReceiveRequest(null, new JObject(), "/acp/ban/", 73, sender);

        assertNotNull(receiver.context);
        assertEquals(73, receiver.context.getRequestId());
        assertEquals("test-endpoint", receiver.context.getEndpoint().getEndpointId());
    }
}
