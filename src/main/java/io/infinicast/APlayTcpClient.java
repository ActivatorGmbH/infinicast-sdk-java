package io.infinicast;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * Created by Michael on 31.07.2015.
 */
public class APlayTcpClient extends IoHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(APlayTcpClient.class);

    private NioSocketConnector connector;

    private IoSession session;

    private static final int CONNECT_TIMEOUT = 5000;

    public APlayTcpClient() {
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new APlayCodecFactory()));
        //connector.getFilterChain().addLast("logger", new LoggingFilter());
    }

    public void setIoHandler(IoHandler handler) {
        connector.setHandler(handler);
    }

    public String connect(SocketAddress address) {
        try {
            synchronized (connector) {
                logger.info("Conecting to " + address.toString());
                ConnectFuture future = connector.connect(address);
                future.awaitUninterruptibly();
                logger.info("Connection established");
                session = future.getSession();
            }
            return null;
        } catch (RuntimeIoException e) {
            logger.warn("Failed to connect.", e);
            return e.toString();
        }
    }

    public ConnectFuture connectAsync(SocketAddress address) {
        throw new UnsupportedOperationException();
        /*
        logger.debug("connecting async to " + address.toString());
		ConnectFuture future = connector.connect(address);
		// TODO: Assign session on successful connect
		// TODO: Notify if connection has been successful?
		return future;
		*/
    }

    public void send(IMessage message) {

        logger.debug("sendToServer " + message.toString());
        IoSession _session = session; // copy session to local variable ensure no threading exception occurs problems
        if (_session != null) {
            _session.write(message);
        } else {
            synchronized (connector) { // if the sesion is not yet set we are in connection mode. in this mode we just have to wait until the session exists.
                _session = session;
                if (_session == null) {
                    logger.error("Cannot send, not connected");
                } else {
                    _session.write(message);
                }
            }
        }
    }

    public boolean disconnect() {
        session = null;
        logger.info("Disconnecting");
        connector.dispose();
        return true;
    }

    /**
     * Returns the currently active session (or null, if none)
     *
     * @return
     */
    public IoSession getSession() {
        return this.session;
    }
}

