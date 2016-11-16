package io.infinicast;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.List;

public class TcpEndpoint2ServerNetLayer extends IoHandlerAdapter implements IEndpoint2ServerNetLayer {
    APlayTcpClient client;
    IEndpoint2ServerNetLayerHandler handler;
    IEndpointAddress publicAddress;
    private Logger logger = LoggerFactory.getLogger(TcpEndpoint2ServerNetLayer.class);

    public TcpEndpoint2ServerNetLayer() {
        client = new APlayTcpClient();
        client.setIoHandler(this);
    }

    @Override
    public String open(IEndpoint2ServerNetSettings settings) {
        handler = settings.getHandler();
        InfinicastServerAddress address = settings.getServerAddress();
        if (!(address instanceof TcpEndpointAddress))
            throw new IllegalArgumentException();
        SocketAddress socketAddress = ((TcpEndpointAddress) address).getSocketAddress();
        return client.connect(socketAddress);
    }

    public String Open(IEndpoint2ServerNetSettings settings) {
        return this.open(settings);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.info("connection closed");
        super.sessionClosed(session);
        handler.onDisconnect();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.warn("Exception caught in session ", cause);
    }

    @Override
    public void closeDirect() {
        client.disconnect();
    }

    @Override
    public void closeAndWait() {
        // TODO: Wait for something
        closeDirect();
    }

    @Override
    public void Close() {
        closeDirect();
    }

    @Override
    public void SendToServer(APlayStringMessage message) {

        client.send(message);
    }

    @Override
    public void SendToServer(List<APlayStringMessage> messages) {
        for (APlayStringMessage message : messages) {
            SendToServer(message);
        }
    }

    @Override
    public IEndpointAddress getPublicAddress() {
        return this.publicAddress;
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        logger.debug("messageReceived " + session.toString() + " " + message);
        if (message instanceof APlayStringMessage) {
            handler.onReceiveFromServer((APlayStringMessage) message);
        } else if (message instanceof LowlevelIntroductionMessage) {
            LowlevelIntroductionMessage welcomeMsg = (LowlevelIntroductionMessage) message;
            this.publicAddress = new TcpEndpointAddress(welcomeMsg.getAddressString());
            logger.debug("Received welcome message, our public address is " + this.publicAddress.getAddress());
            // Logical connection is now complete
            handler.onConnect();
        } else if (message instanceof LowLevelPingMessage) {
            LowLevelPingMessage pingMessage = (LowLevelPingMessage) message;
            logger.debug("LowLevelPingMessage received, answering");
            client.send(new LowLevelPongMessage());
        } else {
            logger.error("received message i don't understand " + message);
        }
    }
}

