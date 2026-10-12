package io.infinicast.client.impl.query;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.errors.ICErrorType;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.messaging.handlers.DCloudMessageHandler;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.impl.pathAccess.RequestResponder;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import org.junit.Test;
import org.slf4j.Logger;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

public class RequestLoggingTest {
    @Test
    public void inboundRequestDoesNotLogEndpointDataOrInfoPerRequest() throws Exception {
        Field loggerField = PathQueryWithHandlerExecutor.class.getDeclaredField("_logger");
        loggerField.setAccessible(true);
        Object originalLogger = loggerField.get(null);
        Logger logger = mock(Logger.class);
        loggerField.set(null, logger);
        try {
            ConnectorMessageManager messages = mock(ConnectorMessageManager.class);
            IConnector connector = mock(IConnector.class);
            PathImpl path = new PathImpl("/acp/auth/");
            PathQueryWithHandlerExecutor executor = new PathQueryWithHandlerExecutor(connector, path, messages);
            executor.onRequest((json, responder, context) -> responder.respond(new JObject()));

            ArgumentCaptor<DCloudMessageHandler> handler = ArgumentCaptor.forClass(DCloudMessageHandler.class);
            verify(messages).addHandler(eq(false), eq(Connector2EpsMessageType.Request), eq(path),
                    handler.capture(), isNull(), isNull());
            IPathAndEndpointContext context = mock(IPathAndEndpointContext.class);
            IEndpoint endpoint = mock(IEndpoint.class);
            when(context.getPath()).thenReturn(path);
            when(context.getEndpoint()).thenReturn(endpoint);
            when(endpoint.getEndpointId()).thenReturn("private-endpoint");
            when(context.getEndpointData()).thenReturn(JObject.Parse("{\"credential\":\"must-not-be-logged\"}"));

            handler.getValue().accept(new JObject(), null, context, 11);

            verify(context, never()).getEndpointData();
            verify(logger, never()).info(anyString());
            verify(logger).debug("received request id={}", 11);
        } finally {
            loggerField.set(null, originalLogger);
        }
    }

    @Test
    public void responsesDoNotLogPathEndpointOrInfoPerRequest() throws Exception {
        Field loggerField = RequestResponder.class.getDeclaredField("_logger");
        loggerField.setAccessible(true);
        Object originalLogger = loggerField.get(null);
        Logger logger = mock(Logger.class);
        loggerField.set(null, logger);
        try {
            ConnectorMessageManager messages = mock(ConnectorMessageManager.class);
            PathImpl path = new PathImpl("/sensitive-path/");
            new RequestResponder(messages, path, "private-endpoint", 12).respond(new JObject());
            new RequestResponder(messages, path, "private-endpoint", 13).respondWithError(
                    ICError.create(ICErrorType.InternalError, "sensitive error", path.toString()));

            verify(logger, never()).info(anyString());
            verify(logger).debug("response id={}", 12);
            verify(logger).debug("error response id={}", 13);
        } finally {
            loggerField.set(null, originalLogger);
        }
    }
}
