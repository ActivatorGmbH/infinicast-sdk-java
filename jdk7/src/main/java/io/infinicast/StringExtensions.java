package io.infinicast;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StringExtensions {
    public static boolean IsNullOrEmpty(String rhs) {
        if (null == rhs) return true;
        return rhs.isEmpty();
    }

    public static String butLast(String rhs) {
        return rhs.substring(0, rhs.length() - 1);
    }

    public static List<String> splitAsPath(String rhs) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : rhs.split("/"))
            result.add(s);
        return result;
    }

    public static String removeFrom(String rhs, int index) {
        return rhs.substring(0, index);
    }

    public static boolean areEqual(String lhs, String rhs) {
        if (null == lhs) return null == rhs;
        if (null == rhs) return null == lhs;
        return lhs.equals(rhs);
    }

    public static String formatException(Exception rhs) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(buffer);
        rhs.printStackTrace(ps);
        return rhs.getMessage() + "\r\n" + new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }
}



