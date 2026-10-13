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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestContextProvenanceTest {
    private static final class CapturingReceiver extends ConnectorMessageReceiver {
        IPathAndEndpointContext context;

        CapturingReceiver() { super(mock(ConnectorMessageManager.class)); }

        @Override
        boolean callHandlers(ICError error, String path, Connector2EpsMessageType type,
                             JObject data, IPathAndEndpointContext requestContext, int requestId) {
            context = requestContext;
            return true;
        }
    }

    @Test
    public void clientEndpointDataCannotSupplyProvenanceAndOnlyAuthSeesTrustedEnvelope() {
        CapturingReceiver receiver = receiver();
        JObject sender = sender();
        JObject clientData = new JObject();
        clientData.set("coreAuditTraceId", "FAKE999");
        clientData.set("coreTrustedClientIp", "192.0.2.99");
        clientData.set("coreTrustedIpSource", "trusted_ingress_proxy_protocol");
        sender.set("data", clientData);

        receiver.onReceiveRequest(null, new JObject(), "/acp/auth/", 73, sender);
        assertNull(receiver.context.getCoreAuditTraceId());
        assertNull(receiver.context.getCoreTrustedClientIp());

        sender.set("coreAuditTraceId", "EPS12345");
        sender.set("coreTrustedClientIp", "203.0.113.7");
        sender.set("coreTrustedIpSource", "trusted_ingress_proxy_protocol");
        receiver.onReceiveRequest(null, new JObject(), "/acp/auth/", 74, sender);
        assertEquals("EPS12345", receiver.context.getCoreAuditTraceId());
        assertEquals("203.0.113.7", receiver.context.getCoreTrustedClientIp());
        assertEquals("trusted_ingress_proxy_protocol", receiver.context.getCoreTrustedIpSource());
        assertEquals("192.0.2.99", receiver.context.getEndpointData().getString("coreTrustedClientIp"));

        receiver.onReceiveRequest(null, new JObject(), "/room/chat", 75, sender);
        assertNull(receiver.context.getCoreTrustedClientIp());
        assertNull(receiver.context.getCoreTrustedIpSource());
    }

    @Test
    public void malformedTraceSourceOrIpFailsClosed() {
        CapturingReceiver receiver = receiver();
        JObject sender = sender();
        sender.set("coreAuditTraceId", "EPS123\nforged=1");
        sender.set("coreTrustedClientIp", "203.0.113.7");
        sender.set("coreTrustedIpSource", "trusted_ingress_proxy_protocol");
        receiver.onReceiveRequest(null, new JObject(), "/acp/auth/", 76, sender);
        assertNull(receiver.context.getCoreTrustedClientIp());

        sender.set("coreAuditTraceId", "EPS12345");
        sender.set("coreTrustedClientIp", "attacker.example");
        receiver.onReceiveRequest(null, new JObject(), "/acp/auth/", 77, sender);
        assertNull(receiver.context.getCoreTrustedClientIp());

        sender.set("coreTrustedClientIp", "2001:db8::2");
        sender.set("coreTrustedIpSource", "X-Forwarded-For");
        receiver.onReceiveRequest(null, new JObject(), "/acp/auth/", 78, sender);
        assertNull(receiver.context.getCoreTrustedClientIp());

        sender.set("coreTrustedIpSource", "trusted_ingress_proxy_protocol");
        receiver.onReceiveRequest(null, new JObject(), "/acp/auth/", 79, sender);
        assertEquals("2001:db8::2", receiver.context.getCoreTrustedClientIp());
    }

    private static CapturingReceiver receiver() {
        CapturingReceiver receiver = new CapturingReceiver();
        IConnector connector = mock(IConnector.class);
        when(connector.getRootPath()).thenReturn(new PathImpl("/"));
        when(connector.path("/acp/auth/")).thenReturn(new PathImpl("/acp/auth/"));
        when(connector.path("/room/chat")).thenReturn(new PathImpl("/room/chat"));
        receiver.setConnector(connector);
        return receiver;
    }

    private static JObject sender() {
        JObject sender = new JObject();
        sender.set("path", "/");
        sender.set("endpoint", "client-endpoint");
        return sender;
    }
}
