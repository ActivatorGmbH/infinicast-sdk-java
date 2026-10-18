package io.infinicast;

import io.infinicast.client.impl.messaging.sender.MessageSender;
import io.infinicast.client.protocol.messages.Connector2EpsMessage;
import org.junit.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MessageSenderSecretLoggingTest {
    @Test
    public void debugLogNeverContainsProtocolCredentials() throws Exception {
        Logger logger = mock(Logger.class);
        Field loggerField = MessageSender.class.getDeclaredField("_logger");
        loggerField.setAccessible(true);
        Object original = loggerField.get(null);
        loggerField.set(null, logger);
        try {
            IEndpoint2ServerNetLayer connection = mock(IEndpoint2ServerNetLayer.class);
            Connector2EpsMessage message = mock(Connector2EpsMessage.class);
            APlayStringMessage encoded = new APlayStringMessage();
            encoded.setDataAsString("{\"credentials\":{\"serviceSecret\":\"synthetic-secret\"}}");
            when(message.buildStringMessage()).thenReturn(encoded);
            new MessageSender(connection).sendMessage(message);
            verify(connection).SendToServer(encoded);
            verify(logger).debug("Send Message");
            verifyNoMoreInteractions(logger);
        } finally {
            loggerField.set(null, original);
        }
    }

    @Test
    public void sendFailureNeverLogsExceptionPayload() throws Exception {
        Logger logger = mock(Logger.class);
        Field loggerField = MessageSender.class.getDeclaredField("_logger");
        loggerField.setAccessible(true);
        Object original = loggerField.get(null);
        loggerField.set(null, logger);
        try {
            IEndpoint2ServerNetLayer connection = mock(IEndpoint2ServerNetLayer.class);
            Connector2EpsMessage message = mock(Connector2EpsMessage.class);
            APlayStringMessage encoded = new APlayStringMessage();
            encoded.setDataAsString("{\"credentials\":\"synthetic-secret\"}");
            when(message.buildStringMessage()).thenReturn(encoded);
            doThrow(new IllegalStateException("synthetic-secret"))
                    .when(connection).SendToServer(encoded);

            new MessageSender(connection).sendMessage(message);

            verify(connection).SendToServer(encoded);
            verify(logger).debug("Send Message");
            verify(logger).error("Could not send: IllegalStateException");
            verifyNoMoreInteractions(logger);
        } finally {
            loggerField.set(null, original);
        }
    }

    @Test
    public void checkedResponseFailureHasFixedPayloadFreeError() throws Exception {
        IEndpoint2ServerNetLayer connection = mock(IEndpoint2ServerNetLayer.class);
        Connector2EpsMessage message = mock(Connector2EpsMessage.class);
        APlayStringMessage encoded = new APlayStringMessage();
        encoded.setDataAsString("{\"credentials\":\"synthetic-secret\"}");
        when(message.buildStringMessage()).thenReturn(encoded);
        doThrow(new IllegalStateException("synthetic-secret"))
                .when(connection).SendToServer(encoded);

        try {
            new MessageSender(connection).sendMessageChecked(message);
            fail("checked response send must expose transport failure");
        } catch (IllegalStateException failure) {
            assertEquals("transport_send_failed", failure.getMessage());
            assertNull("transport exception may contain protocol data", failure.getCause());
        }
    }
}
