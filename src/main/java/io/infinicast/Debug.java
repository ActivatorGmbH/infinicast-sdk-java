package io.infinicast;

public class Debug {
    public static void Assert(boolean what, String messageIfNotTrue) {
        assert what : messageIfNotTrue;
    }
}


