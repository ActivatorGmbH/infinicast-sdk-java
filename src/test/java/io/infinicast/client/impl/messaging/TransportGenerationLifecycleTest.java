package io.infinicast.client.impl.messaging;

import io.infinicast.APlayStringMessage;
import io.infinicast.Action;
import io.infinicast.HandlerPool;
import io.infinicast.IEndpoint2ServerNetLayerHandler;
import io.infinicast.JObject;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.messaging.receiver.ConnectorMessageReceiver;
import io.infinicast.client.impl.messaging.sender.IMessageSender;
import io.infinicast.client.impl.pathAccess.PathImpl;
import io.infinicast.client.protocol.Connector2EpsMessageType;
import io.infinicast.client.protocol.Eps2ConnectorProtocol;
import io.infinicast.client.protocol.IEndpoint2ConnectorProtocolHandler;
import io.infinicast.client.protocol.Eps2ConnectorMessageTypeConverter;
import io.infinicast.client.protocol.messages.Connector2EpsMessage;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class TransportGenerationLifecycleTest {
    @Test
    public void oldDisconnectCannotRunAfterReplacementHasBound() throws Exception {
        ConnectorMessageManager manager = new ConnectorMessageManager();
        CountDownLatch disconnectEntered = new CountDownLatch(1);
        CountDownLatch replacementBound = new CountDownLatch(1);
        AtomicBoolean disconnectedReplacement = new AtomicBoolean();
        manager.setConnector(connector((name, args) -> {
            if ("triggerDisconnect".equals(name)) {
                disconnectEntered.countDown();
                disconnectedReplacement.set(replacementBound.await(300, TimeUnit.MILLISECONDS));
            }
        }));
        IEndpoint2ServerNetLayerHandler old = manager.bindSender(sender());
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Thread disconnect = new Thread(() -> {
            try { old.onDisconnect(); }
            catch (Throwable error) { failure.set(error); }
        });
        disconnect.start();
        assertTrue("old callback must enter its disconnect decision",
                disconnectEntered.await(2, TimeUnit.SECONDS));
        Thread replacement = new Thread(() -> {
            try { manager.bindSender(sender()); replacementBound.countDown(); }
            catch (Throwable error) { failure.set(error); }
        });
        replacement.start();
        disconnect.join(2000);
        replacement.join(2000);
        assertFalse("disconnect callback must finish", disconnect.isAlive());
        assertFalse("replacement bind must finish", replacement.isAlive());
        assertNull(failure.get());
        assertFalse("old disconnect must finish before a replacement is bound",
                disconnectedReplacement.get());
    }

    @Test
    public void staleInitReplyNeverReachesTheNewClientCallback() {
        Eps2ConnectorMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        AtomicInteger initialized = new AtomicInteger();
        manager.setConnector(connector((name, args) -> {
            if ("onInitConnector".equals(name)) initialized.incrementAndGet();
        }));
        IEndpoint2ServerNetLayerHandler old = manager.bindSender(sender());
        manager.bindSender(sender());
        old.onReceiveFromServer(initReply());
        assertEquals("stale Init must not mutate the replacement client", 0,
                initialized.get());
    }

    @Test
    public void initCallbackFinishesBeforeReplacementCanBind() throws Exception {
        Eps2ConnectorMessageTypeConverter.initialize();
        ConnectorMessageManager manager = new ConnectorMessageManager();
        CountDownLatch initEntered = new CountDownLatch(1);
        CountDownLatch replacementBound = new CountDownLatch(1);
        AtomicBoolean initializedReplacement = new AtomicBoolean();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        manager.setConnector(connector((name, args) -> {
            if ("onInitConnector".equals(name)) {
                initEntered.countDown();
                initializedReplacement.set(replacementBound.await(300, TimeUnit.MILLISECONDS));
            }
        }));
        IEndpoint2ServerNetLayerHandler old = manager.bindSender(sender());
        Thread init = new Thread(() -> {
            try { old.onReceiveFromServer(initReply()); }
            catch (Throwable error) { failure.set(error); }
        });
        init.start();
        assertTrue(initEntered.await(2, TimeUnit.SECONDS));
        Thread replacement = new Thread(() -> {
            try { manager.bindSender(sender()); replacementBound.countDown(); }
            catch (Throwable error) { failure.set(error); }
        });
        replacement.start();
        init.join(2000);
        replacement.join(2000);
        assertFalse(init.isAlive());
        assertFalse(replacement.isAlive());
        assertNull(failure.get());
        assertFalse("old Init callback must finish before replacement binds",
                initializedReplacement.get());
    }

    @Test
    public void oldReplyAndEventDelayedDuringDecodeCannotDispatchAfterRebind() throws Exception {
        ConnectorMessageManager manager = new ConnectorMessageManager();
        manager.setConnector(connector((name, args) -> { }));
        IEndpoint2ServerNetLayerHandler old = manager.bindSender(sender());
        ConnectorMessageReceiver receiver = (ConnectorMessageReceiver) manager.getReceiver();
        AtomicInteger delivered = new AtomicInteger();
        receiver.addResponseHandler(Connector2EpsMessageType.RequestResponse, "42",
                (json, error, context, id) -> delivered.incrementAndGet());
        PathImpl path = new PathImpl("/fixture/");
        receiver.addHandler(Connector2EpsMessageType.Reminder.toString(), path,
                (json, error, context, id) -> delivered.incrementAndGet());
        installImmediateHandlerPool(receiver);
        CountDownLatch decoding = new CountDownLatch(1);
        CountDownLatch resume = new CountDownLatch(1);
        installProtocol(receiver, new Eps2ConnectorProtocol() {
            @Override public void decodeStringMessage(APlayStringMessage frame,
                    IEndpoint2ConnectorProtocolHandler handler) {
                decoding.countDown();
                try { assertTrue(resume.await(2, TimeUnit.SECONDS)); }
                catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError(interrupted);
                }
                handler.onReceiveRequestResponse(null, new JObject(), 42, null);
                handler.onReminderTriggered(null, "/fixture/", new JObject());
            }
        });
        Thread oldReceive = new Thread(() -> old.onReceiveFromServer(new APlayStringMessage()));
        oldReceive.start();
        assertTrue(decoding.await(2, TimeUnit.SECONDS));
        manager.bindSender(sender());
        resume.countDown();
        oldReceive.join(2000);
        assertFalse(oldReceive.isAlive());
        assertEquals("old decoded replies and events must be dropped", 0, delivered.get());
        manager.destroy();
    }

    @Test
    public void queuedOldEventCannotRunAfterRebind() throws Exception {
        ConnectorMessageManager manager = new ConnectorMessageManager();
        manager.setConnector(connector((name, args) -> { }));
        IEndpoint2ServerNetLayerHandler old = manager.bindSender(sender());
        ConnectorMessageReceiver receiver = (ConnectorMessageReceiver) manager.getReceiver();
        AtomicInteger delivered = new AtomicInteger();
        PathImpl path = new PathImpl("/fixture/");
        receiver.addHandler(Connector2EpsMessageType.Reminder.toString(), path,
                (json, error, context, id) -> delivered.incrementAndGet());
        AtomicReference<Action> queued = new AtomicReference<>();
        Field pool = ConnectorMessageReceiver.class.getDeclaredField("handlerPool");
        pool.setAccessible(true);
        pool.set(receiver, new HandlerPool() {
            @Override public void QueueHandlerCall(Action action) { queued.set(action); }
        });
        installProtocol(receiver, new Eps2ConnectorProtocol() {
            @Override public void decodeStringMessage(APlayStringMessage frame,
                    IEndpoint2ConnectorProtocolHandler handler) {
                handler.onReminderTriggered(null, "/fixture/", new JObject());
            }
        });
        old.onReceiveFromServer(new APlayStringMessage());
        assertNotNull("fixture must queue the event before rebind", queued.get());
        manager.bindSender(sender());
        queued.get().accept();
        assertEquals("a queued old event must not mutate the replacement", 0,
                delivered.get());
        manager.destroy();
    }

    private static void installProtocol(ConnectorMessageReceiver receiver,
            Eps2ConnectorProtocol protocol) throws Exception {
        Field field = ConnectorMessageReceiver.class.getDeclaredField("_receiveProtocol");
        field.setAccessible(true);
        field.set(receiver, protocol);
    }

    private static void installImmediateHandlerPool(ConnectorMessageReceiver receiver)
            throws Exception {
        Field pool = ConnectorMessageReceiver.class.getDeclaredField("handlerPool");
        pool.setAccessible(true);
        pool.set(receiver, new HandlerPool() {
            @Override public void QueueHandlerCall(Action action) { action.accept(); }
        });
    }

    private interface Invocation { void accept(String name, Object[] args) throws Exception; }

    private static IConnector connector(Invocation invocation) {
        return (IConnector) Proxy.newProxyInstance(IConnector.class.getClassLoader(),
                new Class<?>[]{IConnector.class}, (proxy, method, args) -> {
                    invocation.accept(method.getName(), args);
                    return null;
                });
    }

    private static IMessageSender sender() {
        return new IMessageSender() {
            @Override public void sendMessage(Connector2EpsMessage message) { }
            @Override public void destroy() { }
        };
    }

    private static APlayStringMessage initReply() {
        JObject reply = new JObject();
        reply.set("t", 3);
        reply.set("data", new JObject());
        JObject endpoint = new JObject();
        endpoint.set("endpoint", "fixture");
        endpoint.set("path", "/~endpoints/fixture/");
        reply.set("endpointObject", endpoint);
        APlayStringMessage frame = new APlayStringMessage();
        frame.setDataAsJson(reply);
        return frame;
    }
}
