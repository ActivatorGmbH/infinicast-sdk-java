package io.infinicast.client.impl.messaging;

import io.infinicast.JObject;
import io.infinicast.APlayStringMessage;
import io.infinicast.IEndpoint2ServerNetLayerHandler;
import io.infinicast.IEndpoint2ServerNetLayer;
import io.infinicast.client.api.paths.SetDataDiagnosticOrigin;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.InfinicastClient;
import io.infinicast.client.impl.messaging.sender.IMessageSender;
import io.infinicast.client.impl.messaging.sender.MessageSender;
import io.infinicast.client.impl.messaging.receiver.ConnectorMessageReceiver;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.protocol.Connector2EpsMessageTypeConverter;
import io.infinicast.client.protocol.Eps2ConnectorMessageTypeConverter;
import io.infinicast.client.protocol.messages.Connector2EpsMessage;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static org.junit.Assert.*;

/** Actual PathImpl -> manager -> encoded SetObjectData frame, with no server capability. */
public class SetDataJoinFrameContractTest {
    @Test
    public void unsupportedCoreNeverGetsUniqueMarkerOrFalsePositiveObserver() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        AtomicReference<Connector2EpsMessage> sent = new AtomicReference<>();
        manager.setSender(new IMessageSender() {
            @Override public void sendMessage(Connector2EpsMessage message) { sent.set(message); }
            @Override public void destroy() { }
        });
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        AtomicReference<String> observed = new AtomicReference<>("not-called");
        AtomicReference<CompletableFuture<Void>> observedFuture = new AtomicReference<>();
        BiConsumer<String, CompletableFuture<Void>> observer = (marker, future) -> {
            observed.set(marker);
            observedFuture.set(future);
        };

        Method method = PathImpl.class.getMethod("setDataAsync", JObject.class,
                SetDataDiagnosticOrigin.class, BiConsumer.class);
        @SuppressWarnings("unchecked")
        CompletableFuture<Void> original = (CompletableFuture<Void>) method.invoke(path,
                new JObject(), SetDataDiagnosticOrigin.AUTH_NORMAL, observer);

        assertSame("observer must identify this exact Future", original, observedFuture.get());
        assertNull("old Core must be reported unknown", observed.get());
        assertNotNull("original send must still occur", sent.get());
        JObject actualFrame = JObject.Parse(sent.get().buildStringMessage().getDataAsString());
        assertNull("old Core must not receive an unknown identifier field",
                actualFrame.get("authJoinMarkerV1"));
        assertNull("feature-off must preserve the legacy SetData wire frame",
                actualFrame.get("diagnosticOrigin"));
        path.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_BAN);
        JObject twoArgumentFrame = JObject.Parse(sent.get().buildStringMessage().getDataAsString());
        assertNull("two-argument feature-off call must keep legacy wire format",
                twoArgumentFrame.get("diagnosticOrigin"));
        assertNull(twoArgumentFrame.get("authJoinMarkerV1"));
    }

    @Test
    public void capabilityBindsUniqueMarkerToActualFrameAndOriginalFuture() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        Eps2ConnectorMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        manager.setConnector(connector("svc.gameRoom"));
        AtomicReference<Connector2EpsMessage> sent = new AtomicReference<>();
        IEndpoint2ServerNetLayerHandler bound = manager.bindSender(capturingSender(sent));
        bound.onReceiveFromServer(initReply(Boolean.TRUE));
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        AtomicReference<String> observed = new AtomicReference<>();
        AtomicReference<CompletableFuture<Void>> observedFuture = new AtomicReference<>();

        CompletableFuture<Void> original = path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL, (marker, future) -> {
                    observed.set(marker);
                    observedFuture.set(future);
                });
        JObject frame = JObject.Parse(sent.get().buildStringMessage().getDataAsString());
        assertSame(original, observedFuture.get());
        assertNotNull("capability should produce a marker", observed.get());
        assertTrue("marker must be a private 128-bit lowercase hex value",
                observed.get().matches("[0-9a-f]{32}"));
        assertEquals("observer must see the key on this actual frame",
                observed.get(), frame.getString("authJoinMarkerV1"));
        assertEquals("AUTH_NORMAL", frame.getString("diagnosticOrigin"));
        assertNull("marker must stay outside application data",
                frame.getJObject("data").get("authJoinMarkerV1"));
        int requestId = frame.getInt("requestId");
        ((ConnectorMessageReceiver) manager.getReceiver())
                .onReceiveRequestResponse(null, new JObject(), requestId, null);
        original.get(3, TimeUnit.SECONDS);
        String firstMarker = observed.get();
        path.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_NORMAL,
                (marker, future) -> observed.set(marker));
        assertNotEquals("a second SetData must get a different request key",
                firstMarker, observed.get());
        bound.onDisconnect();
        path.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_NORMAL,
                (marker, future) -> observed.set(marker));
        assertNull("a disconnected transport must lose its join capability",
                observed.get());
        manager.destroy();
    }

    @Test
    public void staleTransportAndStringCapabilityCannotEmitMarker() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        Eps2ConnectorMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        manager.setConnector(connector("svc.gameRoom"));
        AtomicReference<Connector2EpsMessage> sent = new AtomicReference<>();
        IEndpoint2ServerNetLayerHandler oldTransport = manager.bindSender(capturingSender(sent));
        oldTransport.onReceiveFromServer(initReply(Boolean.TRUE));
        IEndpoint2ServerNetLayerHandler newTransport = manager.bindSender(capturingSender(sent));
        oldTransport.onReceiveFromServer(initReply(Boolean.TRUE));
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        AtomicReference<String> observed = new AtomicReference<>("not-called");
        path.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_BAN,
                (marker, future) -> observed.set(marker));
        assertNull("old transport may not grant capability to replacement", observed.get());
        assertNull(JObject.Parse(sent.get().buildStringMessage().getDataAsString())
                .get("authJoinMarkerV1"));
        newTransport.onReceiveFromServer(initReply("true"));
        path.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_BAN,
                (marker, future) -> observed.set(marker));
        assertNull("string true is not a boolean capability", observed.get());
        manager.destroy();
    }

    @Test
    public void wrongRoleDuplicateInitAndKeepaliveNeverReceiveAuthJoin() {
        Connector2EpsMessageTypeConverter.initialize();
        Eps2ConnectorMessageTypeConverter.initialize();
        ConnectorMessageManager wrongRole = new ConnectorMessageManager();
        wrongRole.setConnector(connector("svc.gameServer"));
        AtomicReference<Connector2EpsMessage> wrongSent = new AtomicReference<>();
        IEndpoint2ServerNetLayerHandler wrongBound = wrongRole.bindSender(capturingSender(wrongSent));
        wrongBound.onReceiveFromServer(initReply(Boolean.TRUE));
        PathImpl wrongPath = new PathImpl("/fixture/");
        wrongPath.setMessageManager(wrongRole);
        AtomicReference<String> wrongObserved = new AtomicReference<>("not-called");
        wrongPath.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_NORMAL,
                (marker, future) -> wrongObserved.set(marker));
        assertNull("non-GameRoom role may not receive join capability", wrongObserved.get());
        wrongRole.destroy();

        ConnectorMessageManager gameRoom = new ConnectorMessageManager();
        gameRoom.setConnector(connector("svc.gameRoom"));
        AtomicReference<Connector2EpsMessage> sent = new AtomicReference<>();
        IEndpoint2ServerNetLayerHandler bound = gameRoom.bindSender(capturingSender(sent));
        bound.onReceiveFromServer(initReply(Boolean.TRUE));
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(gameRoom);
        AtomicReference<String> observed = new AtomicReference<>("not-called");
        path.setDataAsync(new JObject(), SetDataDiagnosticOrigin.GS_KEEPALIVE,
                (marker, future) -> observed.set(marker));
        assertNull("GameServer keepalive is never an auth join", observed.get());
        assertNull(JObject.Parse(sent.get().buildStringMessage().getDataAsString())
                .get("authJoinMarkerV1"));

        bound.onReceiveFromServer(initReply(Boolean.TRUE));
        path.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_NORMAL,
                (marker, future) -> observed.set(marker));
        assertNull("duplicate InitConnector reply must revoke join capability", observed.get());
        gameRoom.destroy();
    }

    @Test
    public void observerExceptionDoesNotSuppressTheOriginalSend() {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        AtomicReference<Connector2EpsMessage> sent = new AtomicReference<>();
        manager.setSender(capturingSender(sent));
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        CompletableFuture<Void> original = path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL, (marker, future) -> {
                    throw new IllegalStateException("synthetic-observer-failure");
                });
        assertNotNull("observer failure may not drop original write", sent.get());
        assertFalse("observer failure may not complete original Future", original.isDone());
        manager.destroy();
    }

    @Test
    public void observerIsAttachedBeforeAReplyCanCompleteOriginalFuture() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        Eps2ConnectorMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        manager.setConnector(connector("svc.gameRoom"));
        AtomicReference<CompletableFuture<Void>> observedFuture = new AtomicReference<>();
        AtomicBoolean repliedBeforeObserver = new AtomicBoolean();
        manager.setSender(new IMessageSender() {
            @Override public void sendMessage(Connector2EpsMessage message) {
                int requestId = message.getRequestId();
                ((ConnectorMessageReceiver) manager.getReceiver())
                        .onReceiveRequestResponse(null, new JObject(), requestId, null);
                repliedBeforeObserver.set(observedFuture.get() == null);
            }
            @Override public void destroy() { }
        });
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        CompletableFuture<Void> original = path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL, (marker, future) ->
                        observedFuture.set(future));
        assertSame(original, observedFuture.get());
        assertFalse("a fast reply must not outrun the request observer",
                repliedBeforeObserver.get());
        manager.destroy();
    }

    @Test
    public void observerRebindCannotSendOldFrameOnReplacementTransport() {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        AtomicInteger oldSends = new AtomicInteger();
        AtomicInteger newSends = new AtomicInteger();
        manager.setSender(countingSender(oldSends));
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);

        CompletableFuture<Void> original = path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL,
                (marker, future) -> manager.setSender(countingSender(newSends)));

        assertTrue("a frame prepared for the old transport must fail closed",
                original.isCompletedExceptionally());
        assertEquals(0, oldSends.get());
        assertEquals("an old frame must never cross onto the new sender", 0,
                newSends.get());
        manager.destroy();
    }

    @Test
    public void repeatedCanceledRebindsDoNotRetainResponseHandlers() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        manager.setSender(countingSender(new AtomicInteger()));
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        Field handlerMapField = ConnectorMessageReceiver.class.getDeclaredField("_handlerMap");
        handlerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, ?> handlers = (Map<String, ?>) handlerMapField.get(manager.getReceiver());

        for (int i = 0; i < 50; i++) {
            CompletableFuture<Void> original = path.setDataAsync(new JObject(),
                    SetDataDiagnosticOrigin.AUTH_NORMAL,
                    (marker, future) -> manager.setSender(countingSender(new AtomicInteger())));
            assertTrue(original.isCompletedExceptionally());
            assertEquals("canceled request handlers must not retain the Future", 0,
                    handlers.size());
        }
        manager.destroy();
    }

    @Test
    public void inlineFutureCompletionDoesNotInvertClientAndManagerLocks() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        InfinicastClient client = new InfinicastClient();
        manager.setConnector(client);
        manager.setSender(countingSender(new AtomicInteger()));
        PathImpl path = new PathImpl("/fixture/");
        CountDownLatch completing = new CountDownLatch(1);
        CountDownLatch binderHasClientLock = new CountDownLatch(1);
        CompletableFuture<Void> original = new CompletableFuture<>();
        original.whenComplete((value, error) -> {
            completing.countDown();
            try { binderHasClientLock.await(2, TimeUnit.SECONDS); }
            catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
                throw new AssertionError(interrupted);
            }
            synchronized (client.transportLifecycleLock()) { }
        });
        Thread dispatch = new Thread(() -> manager.sendObservedSetData(path,
                new JObject(), SetDataDiagnosticOrigin.AUTH_NORMAL, original,
                (marker, future) -> manager.setSender(countingSender(new AtomicInteger())),
                (json, error, context) -> { }));
        dispatch.setDaemon(true);
        dispatch.start();
        assertTrue(completing.await(2, TimeUnit.SECONDS));
        Thread binder = new Thread(() -> {
            synchronized (client.transportLifecycleLock()) {
                binderHasClientLock.countDown();
                manager.bindSender(countingSender(new AtomicInteger()));
            }
        });
        binder.setDaemon(true);
        binder.start();
        dispatch.join(2500);
        binder.join(2500);
        assertFalse("Future callback must not hold the manager lock", dispatch.isAlive());
        assertFalse("binder must not wait for a callback holding manager lock", binder.isAlive());
        manager.destroy();
    }

    @Test
    public void admittedSendRemainsBoundToOldSenderDuringRetirement() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        CountDownLatch inSend = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        AtomicInteger oldSends = new AtomicInteger();
        manager.setSender(new IMessageSender() {
            @Override public void sendMessage(Connector2EpsMessage message) {
                inSend.countDown();
                try { assertTrue(release.await(2, TimeUnit.SECONDS)); }
                catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError(interrupted);
                }
                oldSends.incrementAndGet();
            }
            @Override public void destroy() { }
        });
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        Thread send = new Thread(() -> path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL, (marker, future) -> { }));
        send.start();
        assertTrue(inSend.await(2, TimeUnit.SECONDS));
        manager.retireTransport();
        CompletableFuture<Void> rejected = path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL, (marker, future) -> { });
        release.countDown();
        send.join(2000);
        assertFalse(send.isAlive());
        assertEquals("only the send admitted before retirement can reach old sender", 1,
                oldSends.get());
        assertTrue("requests after retirement fail closed", rejected.isCompletedExceptionally());
        manager.destroy();
    }

    @Test
    public void twoArgumentSendFailureReleasesItsResponseHandler() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        manager.setSender(new IMessageSender() {
            @Override public void sendMessage(Connector2EpsMessage message) {
                throw new IllegalStateException("synthetic_send_failure");
            }
            @Override public void destroy() { }
        });
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        Field handlerMapField = ConnectorMessageReceiver.class.getDeclaredField("_handlerMap");
        handlerMapField.setAccessible(true);
        Map<?, ?> handlers = (Map<?, ?>) handlerMapField.get(manager.getReceiver());

        CompletableFuture<Void> result = path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL);

        assertTrue("async caller receives a failed Future", result.isCompletedExceptionally());
        assertEquals("failed send must not retain a Future handler", 0, handlers.size());
        manager.destroy();
    }

    @Test
    public void twoArgumentSendDoesNotMigrateOntoReplacementSender() {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        AtomicInteger oldSends = new AtomicInteger();
        AtomicInteger replacementSends = new AtomicInteger();
        manager.setSender(new IMessageSender() {
            @Override public void sendMessage(Connector2EpsMessage message) {
                manager.setSender(countingSender(replacementSends));
                oldSends.incrementAndGet();
            }
            @Override public void destroy() { }
        });
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);

        path.setDataAsync(new JObject(), SetDataDiagnosticOrigin.AUTH_BAN);

        assertEquals(1, oldSends.get());
        assertEquals("frame admitted on old sender cannot migrate", 0,
                replacementSends.get());
        manager.destroy();
    }

    @Test
    public void realTransportFailureCompletesObservedFutureAndRemovesHandler() throws Exception {
        Connector2EpsMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        IEndpoint2ServerNetLayer failingLayer = (IEndpoint2ServerNetLayer)
                Proxy.newProxyInstance(IEndpoint2ServerNetLayer.class.getClassLoader(),
                        new Class<?>[]{IEndpoint2ServerNetLayer.class},
                        (proxy, method, args) -> {
                            if ("SendToServer".equals(method.getName())) {
                                throw new IllegalStateException("fixture_transport_failure");
                            }
                            return null;
                        });
        manager.setSender(new MessageSender(failingLayer));
        PathImpl path = new PathImpl("/fixture/");
        path.setMessageManager(manager);
        Field handlerMapField = ConnectorMessageReceiver.class.getDeclaredField("_handlerMap");
        handlerMapField.setAccessible(true);
        Map<?, ?> handlers = (Map<?, ?>) handlerMapField.get(manager.getReceiver());

        CompletableFuture<Void> observed = path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_NORMAL, (marker, future) -> { });

        assertTrue("real transport failure must fail the original Future",
                observed.isCompletedExceptionally());
        assertEquals("real transport failure must release the response handler", 0,
                handlers.size());
        CompletableFuture<Void> twoArgument = path.setDataAsync(new JObject(),
                SetDataDiagnosticOrigin.AUTH_BAN);
        assertTrue("two-argument response send must also fail its Future",
                twoArgument.isCompletedExceptionally());
        assertEquals("two-argument failure must also release its handler", 0,
                handlers.size());
        manager.destroy();
    }

    private static IMessageSender countingSender(AtomicInteger sends) {
        return new IMessageSender() {
            @Override public void sendMessage(Connector2EpsMessage message) {
                sends.incrementAndGet();
            }
            @Override public void destroy() { }
        };
    }

    private static IMessageSender capturingSender(AtomicReference<Connector2EpsMessage> sent) {
        return new IMessageSender() {
            @Override public void sendMessage(Connector2EpsMessage message) { sent.set(message); }
            @Override public void destroy() { }
        };
    }

    private static IConnector connector(String role) {
        return (IConnector) Proxy.newProxyInstance(IConnector.class.getClassLoader(),
                new Class<?>[]{IConnector.class}, (proxy, method, args) -> {
                    if ("getRole".equals(method.getName())) { return role; }
                    return null;
                });
    }

    private static APlayStringMessage initReply(Object capability) {
        JObject reply = new JObject();
        reply.set("t", 3);
        JObject data = new JObject();
        data.set("authJoinMarkerV1Enabled", capability);
        reply.set("data", data);
        JObject endpoint = new JObject();
        endpoint.set("endpoint", "fixture-connection");
        endpoint.set("path", "/~endpoints/fixture-connection/");
        reply.set("endpointObject", endpoint);
        APlayStringMessage frame = new APlayStringMessage();
        frame.setDataAsJson(reply);
        return frame;
    }
}
