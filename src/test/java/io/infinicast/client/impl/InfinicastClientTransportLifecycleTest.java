package io.infinicast.client.impl;

import io.infinicast.APlayStringMessage;
import io.infinicast.IEndpoint2ServerNetLayer;
import io.infinicast.IEndpoint2ServerNetLayerHandler;
import io.infinicast.IEndpoint2ServerNetSettings;
import io.infinicast.IEndpointAddress;
import io.infinicast.JObject;
import io.infinicast.client.api.paths.SetDataDiagnosticOrigin;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.messaging.sender.MessageSender;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.protocol.Eps2ConnectorMessageTypeConverter;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class InfinicastClientTransportLifecycleTest {
    @Test
    public void oldCloseCallbackCannotTearDownConnectionOpenedDuringCleanup() {
        InfinicastClient client = new InfinicastClient();
        client.initialize();
        ConnectorMessageManager oldManager = client.messageManager;
        oldManager.setConnector(client);
        FakeLayer oldLayer = new FakeLayer();
        client._endpoint2ServerNetLayer = oldLayer;
        IEndpoint2ServerNetLayerHandler oldHandler =
                oldManager.bindSender(new MessageSender(oldLayer));
        FakeLayer replacement = new FakeLayer();
        oldLayer.onClose = () -> {
            client.initialize();
            client.messageManager.setConnector(client);
            client._endpoint2ServerNetLayer = replacement;
            client.messageManager.bindSender(new MessageSender(replacement));
            oldHandler.onDisconnect();
        };

        oldHandler.onDisconnect();

        assertNotSame("a replacement manager must survive old close", oldManager,
                client.messageManager);
        assertSame("old close must not clear the replacement layer", replacement,
                client._endpoint2ServerNetLayer);
        assertEquals(1, oldLayer.closed.get());
        assertEquals(0, replacement.closed.get());
        client.disconnect();
        assertEquals(1, replacement.closed.get());
    }

    @Test
    public void explicitDisconnectRevokesRetainedPathsAndMarkerCapability() throws Exception {
        Eps2ConnectorMessageTypeConverter.initialize();
        InfinicastClient client = new InfinicastClient();
        client.initialize();
        client.setRole("svc.gameRoom");
        client._onConnect = error -> { };
        ConnectorMessageManager oldManager = client.messageManager;
        oldManager.setConnector(client);
        FakeLayer layer = new FakeLayer();
        client._endpoint2ServerNetLayer = layer;
        IEndpoint2ServerNetLayerHandler bound = oldManager.bindSender(new MessageSender(layer));
        bound.onReceiveFromServer(initReply());
        PathImpl retained = new PathImpl("/fixture/");
        retained.setMessageManager(oldManager);
        AtomicReference<String> marker = new AtomicReference<>();
        retained.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_NORMAL,
                (observed, future) -> marker.set(observed));
        assertNotNull("fixture must have acquired the capability", marker.get());
        int sentBefore = layer.sent.get();

        client.disconnect();
        CompletableFuture<Void> after = retained.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL,
                (observed, future) -> marker.set(observed));
        CompletableFuture<Void> legacy = retained.setDataAsync(new JObject());
        CompletableFuture<Void> twoArgument = retained.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_BAN);

        assertTrue("a retained Path must fail promptly after disconnect",
                after.isCompletedExceptionally());
        assertTrue("legacy async SetData must return a failed Future, not throw",
                legacy.isCompletedExceptionally());
        assertTrue("two-argument async SetData must return a failed Future, not throw",
                twoArgument.isCompletedExceptionally());
        Field handlerMapField = oldManager.getReceiver().getClass().getDeclaredField("_handlerMap");
        handlerMapField.setAccessible(true);
        Map<?, ?> handlers = (Map<?, ?>) handlerMapField.get(oldManager.getReceiver());
        for (int i = 0; i < 50; i++) {
            assertTrue(retained.setDataAsync(new JObject()).isCompletedExceptionally());
            assertTrue(retained.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_BAN)
                    .isCompletedExceptionally());
        }
        assertEquals("retained Paths must not accumulate handlers after disconnect", 0,
                handlers.size());
        assertEquals("a retired manager must never send on the old layer",
                sentBefore, layer.sent.get());
        assertEquals(1, layer.closed.get());
    }

    @Test
    public void topLevelInitErrorCompletesConnectCallbackWithoutEndpoint() {
        Eps2ConnectorMessageTypeConverter.initialize();
        InfinicastClient client = new InfinicastClient();
        client.initialize();
        ConnectorMessageManager manager = client.messageManager;
        manager.setConnector(client);
        FakeLayer layer = new FakeLayer();
        client._endpoint2ServerNetLayer = layer;
        AtomicReference<io.infinicast.client.api.errors.ICError> result = new AtomicReference<>();
        client._onConnect = result::set;
        IEndpoint2ServerNetLayerHandler bound = manager.bindSender(new MessageSender(layer));
        JObject reply = new JObject();
        reply.set("t", 3);
        reply.set("errorReason", "InternalError");
        reply.set("errorMsg", "fixture_error");
        APlayStringMessage frame = new APlayStringMessage();
        frame.setDataAsJson(reply);

        bound.onReceiveFromServer(frame);

        assertNotNull("top-level Core init error must reach connect callback", result.get());
        assertNull("failed Init must not install an endpoint", client.getOwnEndpoint());
        client.disconnect();
    }

    @Test
    public void initCallbackMayDisconnectWithoutBlockingSessionCloseCallback() throws Exception {
        Eps2ConnectorMessageTypeConverter.initialize();
        InfinicastClient client = new InfinicastClient();
        client.initialize();
        ConnectorMessageManager manager = client.messageManager;
        manager.setConnector(client);
        FakeLayer layer = new FakeLayer();
        client._endpoint2ServerNetLayer = layer;
        IEndpoint2ServerNetLayerHandler bound = manager.bindSender(new MessageSender(layer));
        layer.onClose = () -> {
            Thread sessionClosed = new Thread(bound::onDisconnect);
            sessionClosed.start();
            try { sessionClosed.join(500); }
            catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new AssertionError(interrupted);
            }
            assertFalse("TCP close must not wait on an Init-held generation lock",
                    sessionClosed.isAlive());
        };
        client._onConnect = error -> client.disconnect();

        bound.onReceiveFromServer(initReply());

        assertEquals(1, layer.closed.get());
        assertNull(client.messageManager);
    }

    @Test
    public void replacementWaitsForRealClientInitCallbackWithoutHoldingNetworkLock()
            throws Exception {
        Eps2ConnectorMessageTypeConverter.initialize();
        InfinicastClient client = new InfinicastClient();
        client.initialize();
        ConnectorMessageManager manager = client.messageManager;
        manager.setConnector(client);
        FakeLayer oldLayer = new FakeLayer();
        client._endpoint2ServerNetLayer = oldLayer;
        IEndpoint2ServerNetLayerHandler bound = manager.bindSender(new MessageSender(oldLayer));
        CountDownLatch callbackEntered = new CountDownLatch(1);
        CountDownLatch replacementBound = new CountDownLatch(1);
        AtomicBoolean observedReplacementDuringOldCallback = new AtomicBoolean();
        client._onConnect = error -> {
            callbackEntered.countDown();
            try { observedReplacementDuringOldCallback.set(
                    replacementBound.await(300, TimeUnit.MILLISECONDS)); }
            catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new AssertionError(interrupted);
            }
        };
        Thread init = new Thread(() -> bound.onReceiveFromServer(initReply()));
        init.start();
        assertTrue(callbackEntered.await(2, TimeUnit.SECONDS));
        Thread replacement = new Thread(() -> {
            manager.bindSender(new MessageSender(new FakeLayer()));
            replacementBound.countDown();
        });
        replacement.start();
        init.join(2000);
        replacement.join(2000);
        assertFalse(init.isAlive());
        assertFalse(replacement.isAlive());
        assertFalse("the old callback must finish before replacement binds",
                observedReplacementDuringOldCallback.get());
        client.disconnect();
    }

    private static APlayStringMessage initReply() {
        JObject reply = new JObject();
        reply.set("t", 3);
        JObject data = new JObject();
        data.set("authJoinMarkerV1Enabled", true);
        reply.set("data", data);
        JObject endpoint = new JObject();
        endpoint.set("endpoint", "fixture");
        endpoint.set("path", "/~endpoints/fixture/");
        reply.set("endpointObject", endpoint);
        APlayStringMessage frame = new APlayStringMessage();
        frame.setDataAsJson(reply);
        return frame;
    }

    private static class FakeLayer implements IEndpoint2ServerNetLayer {
        final AtomicInteger sent = new AtomicInteger();
        final AtomicInteger closed = new AtomicInteger();
        Runnable onClose;
        @Override public String open(IEndpoint2ServerNetSettings settings) { return null; }
        @Override public void closeDirect() { Close(); }
        @Override public void closeAndWait() { Close(); }
        @Override public void Close() {
            closed.incrementAndGet();
            if (onClose != null) onClose.run();
        }
        @Override public void SendToServer(APlayStringMessage message) { sent.incrementAndGet(); }
        @Override public void SendToServer(List<APlayStringMessage> messages) {
            sent.addAndGet(messages.size());
        }
        @Override public IEndpointAddress getPublicAddress() { return null; }
    }
}
