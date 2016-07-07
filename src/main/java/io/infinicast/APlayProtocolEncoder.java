package io.infinicast;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.CharsetEncoder;

/**
 * Created by Michael on 17.08.2015.
 */
public class APlayProtocolEncoder implements ProtocolEncoder {
    private static final CharsetEncoder charsetEncoder = APlayCodec.ENCODING_CHARSET.newEncoder();

    private IoBuffer allocateBuffer(int approxSize) {
        IoBuffer buffer = IoBuffer.allocate(approxSize, false);
        // We don't really know how many bytes will be produced by charsetEncoder, so allow
        // the buffer to expand if our approximation is wrong
        buffer.setAutoExpand(true);
        return buffer;
    }

    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput out) throws Exception {

        if (o instanceof APlayStringMessage) {
            APlayStringMessage msg = (APlayStringMessage) o;

            /*byte[] compressedData =msg.getCompressedData();
            if(compressedData!=null){
                //String payload = msg.getDataAsString();

                //byte[] zipped = StringCompressor.compress(payload);
                int approxBufferSize = 1 + 4 + compressedData.length;
                IoBuffer buffer = allocateBuffer(approxBufferSize);
                buffer.put(APlayCodec.MSGTYPE_PAYLOAD_GZIP);
                buffer.putInt(compressedData.length);
                buffer.put(compressedData);
                buffer.flip();
                out.write(buffer);
            }else{*/
            String payload = msg.getDataAsString();
            int approxBufferSize = 1 + 4 + payload.length();
            IoBuffer buffer = allocateBuffer(approxBufferSize);
            buffer.put(APlayCodec.MSGTYPE_PAYLOAD);
            buffer.putPrefixedString(payload, 4, charsetEncoder);
            buffer.flip();
            out.write(buffer);
            //}
        } else if (o instanceof LowlevelIntroductionMessage) {
            LowlevelIntroductionMessage msg = (LowlevelIntroductionMessage) o;

            IoBuffer buffer = allocateBuffer(64);
            buffer.put(APlayCodec.MSGTYPE_LOWLEVEL_INTRODUCTION);
            buffer.putPrefixedString(msg.getAddressString(), 4, charsetEncoder);
            buffer.flip();
            out.write(buffer);
        } else if (o instanceof LowLevelPingMessage) {
            LowLevelPingMessage msg = (LowLevelPingMessage) o;

            IoBuffer buffer = allocateBuffer(64);
            buffer.put(APlayCodec.MSGTYPE_LOWLEVEL_PING);
            buffer.putInt(8 + 4); // long + int
            buffer.putLong(msg.getPingTime());
            buffer.putInt(msg.getLastRoundTripTime());
            buffer.flip();
            out.write(buffer);
        } else if (o instanceof LowLevelPongMessage) {
            LowLevelPongMessage msg = (LowLevelPongMessage) o;

            IoBuffer buffer = allocateBuffer(64);
            buffer.put(APlayCodec.MSGTYPE_LOWLEVEL_PONG);
            buffer.putInt(8); // long
            buffer.putLong(msg.getPingTime());
            buffer.flip();
            out.write(buffer);
        } else {
            System.err.println("Codec does not support message type: " + o.getClass().getSimpleName());
        }

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}

