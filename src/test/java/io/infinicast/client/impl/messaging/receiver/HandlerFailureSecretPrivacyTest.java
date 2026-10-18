package io.infinicast.client.impl.messaging.receiver;

import io.infinicast.Action;
import io.infinicast.HandlerPool;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.errors.ICErrorType;
import io.infinicast.client.impl.InfinicastClient;
import io.infinicast.client.impl.messaging.PathHandlerContainer;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HandlerFailureSecretPrivacyTest {
    private static final String SYNTHETIC_SECRET = "synthetic-secret";
    private static final String SAFE_ERROR = "internal_exception";

    private static final class RecordingClient extends InfinicastClient {
        private ICError lastError;

        @Override
        public void unhandeledErrorInfo(IPath path, ICError error) {
            lastError = error;
            super.unhandeledErrorInfo(path, error);
        }
    }

    @Test
    public void queuedHandlerFailureNeverForwardsExceptionText() {
        RecordingClient client = new RecordingClient();
        AtomicReference<String> callbackText = new AtomicReference<>();
        client.onUnhandeledError((path, message) -> callbackText.set(message));

        ConnectorMessageReceiver receiver = new ConnectorMessageReceiver(null);
        receiver.setConnector(client);
        receiver.handlerPool = new HandlerPool() {
            @Override
            public void QueueHandlerCall(Action item) {
                throw new IllegalStateException(SYNTHETIC_SECRET);
            }
        };
        PathHandlerContainer bag = receiver.ensureMessageHandlerBag("/fixture/", null);
        bag.addHandler("FixtureType", (data, error, context, requestId) -> {});

        int scheduled = receiver.callHandlersLimitedByString(
                null, "/fixture/", "FixtureType", null, null, 0, 1);

        assertEquals(0, scheduled);
        assertNotNull(client.lastError);
        assertEquals(ICErrorType.InternalError, client.lastError.getErrorType());
        assertEquals("/fixture/", client.lastError.getPath());
        assertNotNull(callbackText.get());
        assertFalse(callbackText.get().contains(SYNTHETIC_SECRET));
        assertEquals(SAFE_ERROR, callbackText.get());
        assertFalse(client.lastError.toJson().toString().contains(SYNTHETIC_SECRET));
    }

    @Test
    public void exceptionConversionRetainsErrorTypeAndPathWithoutExceptionText() {
        ICError error = ICError.fromException(
                new IllegalStateException(SYNTHETIC_SECRET), "/fixture/");
        assertEquals(ICErrorType.InternalError, error.getErrorType());
        assertEquals("/fixture/", error.getPath());
        assertFalse(error.getMessage().contains(SYNTHETIC_SECRET));
        assertEquals(SAFE_ERROR, error.getMessage());
    }

    @Test
    public void unhandledErrorLoggerReceivesOnlyFixedClass() throws Exception {
        InfinicastClient client = new InfinicastClient();
        Logger logger = mock(Logger.class);
        Field loggerField = InfinicastClient.class.getDeclaredField("_ClientLogger");
        loggerField.setAccessible(true);
        Object original = loggerField.get(null);
        loggerField.set(null, logger);
        try {
            ICError error = ICError.fromException(
                    new IllegalStateException(SYNTHETIC_SECRET), "/fixture/");
            client.unhandeledErrorInfo(null, error);
            verify(logger).error("an Unhandeled Error occured: " + SAFE_ERROR);
            verifyNoMoreInteractions(logger);
        } finally {
            loggerField.set(null, original);
        }
    }
}
