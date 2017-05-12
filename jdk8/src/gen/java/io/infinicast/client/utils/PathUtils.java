package io.infinicast.client.utils;

import io.infinicast.StringExtensions;
import io.infinicast.StringUtils;
import io.infinicast.client.api.IPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class PathUtils {
    public static String infinicastInternStart = "/~IC/";
    public static HashMap<String, String> parsePath(String pathVariables, String path) {
        HashMap<String, String> result = new HashMap<String, String>();
        List<String> splittedVariables = StringExtensions.splitAsPath(pathVariables);
        List<String> splittedPath = StringExtensions.splitAsPath(path);
        for (int i = 0;
         (i < splittedPath.size()); (i)++) {
            if (splittedVariables.size() > i) {
                String variable = splittedVariables.get(i);
                if (variable.startsWith("$")) {
                    result.put(variable.substring(1), splittedPath.get(i));
                }
            }
        }
        return result;
    }
    public static String cleanup(String path) {
        String result = path;
        while (result.contains("//")) {
            result = path.replace("//", "/");
        }
        if (!(result.startsWith("/"))) {
            result = ("/" + result);
        }
        if (!(result.endsWith("/")) && !(result.endsWith("*"))) {
            result = (result + "/");
        }
        return result;
    }
    public static String getObjectListPath(String path) {
        String result = PathUtils.cleanup(path);
        return PathUtils.getParentPath(result);
    }
    static void createWildcardSubElement(String str, List<String> splitted, int index, ArrayList<String> results) {
        if (index == splitted.size()) {
            results.add(str + "/");
        }
        else {
            PathUtils.createWildcardSubElement(((str + "/") + splitted.get(index)), splitted, (index + 1), results);
            PathUtils.createWildcardSubElement((str + "/*"), splitted, (index + 1), results);
        }
    }
    public static ArrayList<String> getWildCardedPaths(String path) {
        ArrayList<String> result = new ArrayList<String>();
        if (StringExtensions.IsNullOrEmpty(path)) {
            result.add("");
            return result;
        }
        String cleanPath = PathUtils.cleanup(path);
        // for c# splitted returns also the last element as empty. This is a difference between java and c# - MAX: how about the overload with StringSplitOptions?
            cleanPath = StringExtensions.butLast(cleanPath);
        List<String> splitted = StringExtensions.splitAsPath(cleanPath);
        PathUtils.createWildcardSubElement("", splitted, 1, result);
        return result;
    }
    public static String combine(String path1, String path2) {
        return PathUtils.cleanup((path1 + "/") + path2);
    }
    public static String combineId(String path, String id) {
        return PathUtils.combine(path, id);
    }
    public static String getParentPath(String path) {
        String result = PathUtils.cleanup(path);
        // all paths will end with / !
            result = StringExtensions.butLast(result);
        if (result.contains("/")) {
            result = result.substring(0, (result.lastIndexOf("/") + 1));
            return result;
        }
        return "";
    }
    public static String getLastPathPart(String key) {
        String str = PathUtils.cleanup(key);
        if (str.endsWith("/")) {
            str = str.substring(0, (str.length() - 1));
        }
        if (str.lastIndexOf("/") != -1) {
            str = str.substring(str.lastIndexOf("/") + 1);
        }
        return str;
    }
    public static String getEndpointPath(String endpointId) {
        return (("/~endpoints/" + endpointId) + "/");
    }
    public static String endpointDisconnectedByRolePath(String role) {
        return PathUtils.infinicastIntern("roleDc/" + role);
    }
    static String infinicastIntern(String s) {
        return (("/~IC/" + s) + "/");
    }
    public static String escape(String str) {
        return str;
    }
    public static String getPathAddressElement(String path, int idx) {
        if (StringExtensions.IsNullOrEmpty(path)) {
            return "";
        }
        String result = PathUtils.cleanup(path);
        return StringUtils.GetStringPathEleByIdx(result, idx);
    }
    public static String pathToString(IPath path) {
        if (path == null) {
            return "";
        }
        return path.toString();
    }
}
