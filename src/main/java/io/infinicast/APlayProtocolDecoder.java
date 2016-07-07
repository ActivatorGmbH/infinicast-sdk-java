package io.infinicast;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.charset.CharsetDecoder;

/**
 * Created by Michael on 17.08.2015.
 */
public class APlayProtocolDecoder extends CumulativeProtocolDecoder {

    private static final String DECODER_STATE_KEY = APlayProtocolDecoder.class.getName() + ".STATE";

    private static final CharsetDecoder charsetDecoder = APlayCodec.ENCODING_CHARSET.newDecoder();

    private static class DecoderState {
        Byte messageType;

        public void reset() {
            messageType = null;
        }
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

        DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE_KEY);
        if (decoderState == null) {
            decoderState = new DecoderState();
            session.setAttribute(DECODER_STATE_KEY, decoderState);
        }
        if (decoderState.messageType == null) {
            // State 1: Determine message type
            decoderState.messageType = in.get();
        }


/*		da müsstest du putLong(Array.lenght()) und danach putBytes machen und umgekehrt
        und dann zusätzlich in.prefixedDataAvailable(8)
		dann zeichen lesen, dann in.prefixedDataAvailable(8+len);
		wenn da true rauskommt kannst du dann getLong() und getBytes(len) machen*/
        switch (decoderState.messageType) {
        case APlayCodec.MSGTYPE_PAYLOAD:
            if (in.prefixedDataAvailable(4)) {
                String payload = in.getPrefixedString(4, charsetDecoder);
                APlayStringMessage msg = new APlayStringMessage();
                msg.setDataAsString(payload);
                out.write(msg);
                decoderState.reset();
                return true;
            } else {
                // We need to wait for more data
                return false;
            }
        case APlayCodec.MSGTYPE_PAYLOAD_GZIP:
            if (in.prefixedDataAvailable(4)) {
                byte[] payload = getPrefixedData(in);
                APlayStringMessage msg = new APlayStringMessage();
                msg.setDataAsString(StringCompressor.decompress(payload));
                out.write(msg);
                decoderState.reset();
                return true;
            } else {
                // We need to wait for more data
                return false;
            }
        case APlayCodec.MSGTYPE_LOWLEVEL_PING: {
            if (in.prefixedDataAvailable(4)) {
                DataInputStream inputStream = getPrefixedDataInputStream(in);
                LowLevelPingMessage msg = new LowLevelPingMessage();
                msg.setPingTime(inputStream.readLong());
                msg.setLastRoundTripTime(inputStream.readInt());
                out.write(msg);
                decoderState.reset();
                return true;
            } else {
                // We need to wait for more data
                return false;
            }
        }
        case APlayCodec.MSGTYPE_LOWLEVEL_PONG: {
            if (in.prefixedDataAvailable(4)) {
                DataInputStream inputStream = getPrefixedDataInputStream(in);
                LowLevelPongMessage msg = new LowLevelPongMessage();
                msg.setPingTime(inputStream.readLong());
                out.write(msg);
                decoderState.reset();
                return true;
            } else {
                // We need to wait for more data
                return false;
            }
        }
        case APlayCodec.MSGTYPE_LOWLEVEL_INTRODUCTION:
            if (in.prefixedDataAvailable(4)) {
                String address = in.getPrefixedString(4, charsetDecoder);
                LowlevelIntroductionMessage msg = new LowlevelIntroductionMessage(address);
                out.write(msg);
                decoderState.reset();
                return true;
            } else {
                return false;
            }

        default:
            // Skip all bytes currently read
            in.position(in.limit());
            return true;
        }

    }

    private byte[] getPrefixedData(IoBuffer in) {
        int len = in.getInt();
        byte[] payload = new byte[len];
        in.get(payload);
        return payload;
    }

    private DataInputStream getPrefixedDataInputStream(IoBuffer in) {
        byte[] payload = getPrefixedData(in);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload);
        return new DataInputStream(byteArrayInputStream);
    }
}

