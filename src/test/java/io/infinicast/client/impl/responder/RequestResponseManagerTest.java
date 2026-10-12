package io.infinicast.client.impl.responder;

import io.infinicast.JObject;
import io.infinicast.client.impl.InfinicastClient;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.impl.pathAccess.RequestResponder;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RequestResponseManagerTest {

    @Test
    public void slowResponderLeavesTrackingAfterHandlingStartedButCanAnswerLater() {
        ConnectorMessageManager messages = mock(ConnectorMessageManager.class);
        PathImpl path = new PathImpl("/slow/");
        RequestResponder responder = new RequestResponder(messages, path, "test-endpoint", 7);
        RequestResponseManager manager = new RequestResponseManager();
        manager.addResponder(responder);

        manager.doCheck();
        manager.doCheck();
        assertEquals(1, manager._awaitingResponders.size());
        manager.doCheck();

        verify(messages).sendRequestAnswer(
                Connector2EpsMessageType.RequestHandlingStarted, path, null, "test-endpoint", 7);
        assertEquals(0, manager._awaitingResponders.size());

        JObject answer = new JObject();
        responder.respond(answer);
        verify(messages).sendRequestAnswer(
                Connector2EpsMessageType.RequestResponse, path, answer, "test-endpoint", 7);
    }

    @Test
    public void disconnectDropsPendingTrackingWithoutCancellingResponder() {
        InfinicastClient client = new InfinicastClient();
        RequestResponseManager manager = client.getRequestResponseManager();
        ConnectorMessageManager messages = mock(ConnectorMessageManager.class);
        PathImpl path = new PathImpl("/pending/");
        RequestResponder responder = new RequestResponder(messages, path, "test-endpoint", 8);
        manager.addResponder(responder);

        client.disconnect();

        assertEquals(0, manager._awaitingResponders.size());
        JObject answer = new JObject();
        responder.respond(answer);
        verify(messages).sendRequestAnswer(
                Connector2EpsMessageType.RequestResponse, path, answer, "test-endpoint", 8);
    }

    @Test
    public void completedResponderIsStillReleasedOnNextCheck() {
        ConnectorMessageManager messages = mock(ConnectorMessageManager.class);
        PathImpl path = new PathImpl("/quick/");
        RequestResponder responder = new RequestResponder(messages, path, "test-endpoint", 9);
        RequestResponseManager manager = new RequestResponseManager();
        manager.addResponder(responder);
        responder.respond(new JObject());

        manager.doCheck();

        assertEquals(0, manager._awaitingResponders.size());
    }
}
