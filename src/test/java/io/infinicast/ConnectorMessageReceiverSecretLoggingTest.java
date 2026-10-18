package io.infinicast;

import io.infinicast.client.impl.messaging.receiver.ConnectorMessageReceiver;
import io.infinicast.client.protocol.Eps2ConnectorProtocol;
import org.junit.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ConnectorMessageReceiverSecretLoggingTest {
    @Test
    public void debugAndDecodeFailureNeverLogProtocolOrExceptionPayload() throws Exception {
        Logger logger = mock(Logger.class);
        when(logger.isDebugEnabled()).thenReturn(true);
        Field loggerField = ConnectorMessageReceiver.class.getDeclaredField("_logger");
        loggerField.setAccessible(true);
        Object original = loggerField.get(null);
        loggerField.set(null, logger);
        try {
            ConnectorMessageReceiver receiver = new ConnectorMessageReceiver(null);
            Field protocolField = ConnectorMessageReceiver.class.getDeclaredField("_receiveProtocol");
            protocolField.setAccessible(true);
            Eps2ConnectorProtocol protocol = mock(Eps2ConnectorProtocol.class);
            protocolField.set(receiver, protocol);

            APlayStringMessage message = new APlayStringMessage();
            message.setDataAsString("{\"serviceSecret\":\"synthetic-secret\"}");
            doThrow(new IllegalArgumentException("synthetic-secret"))
                    .when(protocol).decodeStringMessage(any(APlayStringMessage.class), any());

            receiver.receive(message);

            verify(logger).isDebugEnabled();
            verify(logger).debug("received message");
            verify(logger).error("Exception in decode message: IllegalArgumentException");
            verifyNoMoreInteractions(logger);
            verify(protocol).decodeStringMessage(eq(message), eq(receiver));
        } finally {
            loggerField.set(null, original);
        }
    }
}
