package io.infinicast;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by Michael on 17.08.2015.
 */
public class APlayCodecFactory implements ProtocolCodecFactory {

    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;

    public APlayCodecFactory() {
        encoder = new APlayProtocolEncoder();
        decoder = new APlayProtocolDecoder();
    }

    /**
     * Return the singleton encoder
     *
     * @param ioSession
     * @return
     * @throws Exception
     */
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    /**
     * Return the singleton decoder
     *
     * @param ioSession
     * @return
     * @throws Exception
     */
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }


}

