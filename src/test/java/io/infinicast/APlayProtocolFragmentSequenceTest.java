package io.infinicast;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.DefaultTransportMetadata;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class APlayProtocolFragmentSequenceTest {
    @Test
    public void textReplyAfterFragmentedReplyIsDelivered() throws Exception {
        checkEverySplit(false);
    }

    @Test
    public void binaryReplyAfterFragmentedReplyIsDelivered() throws Exception {
        checkEverySplit(true);
    }

    private void checkEverySplit(boolean binary) throws Exception {
        byte[] first = frame(1, binary);
        byte[] second = frame(2, binary);
        byte[] third = frame(3, binary);
        for (int split = 1; split < first.length; split++) {
            DummySession session = new DummySession();
            session.setTransportMetadata(new DefaultTransportMetadata("nio", "tcp", false, true,
                    InetSocketAddress.class, IoSessionConfig.class, IoBuffer.class));
            List<APlayStringMessage> received = new ArrayList<>();
            ProtocolDecoderOutput output = new ProtocolDecoderOutput() {
                public void write(Object message) { received.add((APlayStringMessage) message); }
                public void flush(IoFilter.NextFilter next, IoSession ignored) { }
            };
            APlayProtocolDecoder decoder = new APlayProtocolDecoder();
            try {
                decoder.decode(session, IoBuffer.wrap(Arrays.copyOfRange(first, 0, split)), output);
                assertEquals("incomplete frame, split=" + split, 0, received.size());
                decoder.decode(session, IoBuffer.wrap(Arrays.copyOfRange(first, split, first.length)), output);
                assertEquals("completed fragmented frame, split=" + split, 1, received.size());
                // A fresh network read must still work after the cumulative buffer was consumed.
                decoder.decode(session, IoBuffer.wrap(second), output);
                assertEquals("next reply lost, split=" + split, 2, received.size());
                decoder.decode(session, IoBuffer.wrap(third), output);
                assertEquals("subsequent reply lost, split=" + split, 3, received.size());
                for (int i = 0; i < received.size(); i++) {
                    Number requestId = (Number) received.get(i).getDataAsDecodedJson().get("requestId");
                    assertEquals("reply order, split=" + split, i + 1, requestId.intValue());
                }
            } finally {
                decoder.dispose(session);
            }
        }
    }

    private byte[] frame(int id, boolean binary) throws Exception {
        String text = "{\"t\":2,\"requestId\":" + id + ",\"data\":{}}";
        if (!binary) {
            byte[] json = text.getBytes(StandardCharsets.UTF_8);
            return ByteBuffer.allocate(5 + json.length).put(APlayCodec.MSGTYPE_PAYLOAD)
                    .putInt(json.length).put(json).array();
        }
        APlayStringMessage message = new APlayStringMessage();
        message.setDataAsJson(JObject.Parse(text));
        List<byte[]> frames = new ArrayList<>();
        new APlayProtocolEncoder().encode(null, message, new ProtocolEncoderOutput() {
            public void write(Object encoded) {
                IoBuffer buffer = ((IoBuffer) encoded).duplicate();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                frames.add(bytes);
            }
            public void mergeAll() { }
            public WriteFuture flush() { return null; }
        });
        assertEquals(1, frames.size());
        return frames.get(0);
    }
}
