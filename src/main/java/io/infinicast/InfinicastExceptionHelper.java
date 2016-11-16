package io.infinicast;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by ocean on 16.11.2016.
 */
public class InfinicastExceptionHelper {
    public static String ExceptionToString(Exception x) {
        StringWriter sw = new StringWriter();
        x.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
