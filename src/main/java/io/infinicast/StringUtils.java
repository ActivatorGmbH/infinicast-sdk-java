package io.infinicast;

/**
 * Created by ASG on 21.06.2016.
 */
public class StringUtils {
    public static String GetStringPathEleByIdx(String path, int idx) {
        if (StringExtensions.IsNullOrEmpty(path)) return "";
        StringBuilder sb = new StringBuilder();

        int count = 0;

        boolean inside = false;

        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                if (count == idx) {
                    inside = true;
                } else if (count == idx + 1) {
                    return sb.toString();
                }
                count++;
            } else {
                if (inside) {
                    sb.append(path.charAt(i));
                }
            }
        }
        return "";
    }
}
