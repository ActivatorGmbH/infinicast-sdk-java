package io.infinicast;

import java.nio.charset.Charset;

/**
 * Created by Michael on 17.08.2015.
 */
public class APlayCodec {

    public static final Charset ENCODING_CHARSET = Charset.forName("UTF-8");

    public static final byte MSGTYPE_LOWLEVEL_INTRODUCTION = 0x10;
    public static final byte MSGTYPE_LOWLEVEL_PING = 0x11;
    public static final byte MSGTYPE_LOWLEVEL_PONG = 0x12;
    public static final byte MSGTYPE_PAYLOAD = 0x50;
    public static final byte MSGTYPE_PAYLOAD_GZIP = 0x51;
    public static final byte MSGTYPE_PAYLOAD_BINARY = 0x52;


    public static final byte MSGTYPE_PAYLOAD_BINARY_JSON = 0x53;
}

