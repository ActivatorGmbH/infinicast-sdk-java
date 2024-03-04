package io.infinicast;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

/**
 * Created by Michael on 26.04.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class APlayProtocolDecoderTest {

    @Mock
    IoSession session;

    @Mock
    ProtocolDecoderOutput out;

    @Test
    public void decodesPing() throws Exception {
        // Buffer content
        byte[] bufferState = hexStringToByteArray("110000000C000001544DCBB16F000000000000");
        IoBuffer buffer = IoBuffer.wrap(bufferState);
        APlayProtocolDecoder decoder = new APlayProtocolDecoder();

        assertThat(buffer.position(), is(0));
        boolean packetComplete = decoder.doDecode(session, buffer, out);

        assertThat(packetComplete, is(true));
        assertThat(buffer.position(), is(not(0)));
        verify(out).write(any());
    }

    @Test
    public void unknownMessageClearsBuffer() throws Exception {
        // fe is an unknown message type
        byte[] bufferState = hexStringToByteArray("fe0000000C000001544DCBB16F000000000000");
        IoBuffer buffer = IoBuffer.wrap(bufferState);
        APlayProtocolDecoder decoder = new APlayProtocolDecoder();

        int messageLen = buffer.limit();
        boolean packetComplete = decoder.doDecode(session, buffer, out);

        assertThat(packetComplete, is(true));
        // Decoder has to skip all bytes that belong ot this message
        assertThat(buffer.position(), is(not(0)));
        assertThat(buffer.position(), is(messageLen));
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
