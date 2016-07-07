package io.activator.infinicast.client.utils;
import org.joda.time.DateTime;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.activator.infinicast.client.api.*;
import io.activator.infinicast.client.impl.*;
import io.activator.infinicast.client.utils.*;
import io.activator.infinicast.client.protocol.*;
import io.activator.infinicast.client.api.paths.*;
import io.activator.infinicast.client.api.query.*;
import io.activator.infinicast.client.api.paths.handler.*;
import io.activator.infinicast.client.api.paths.options.*;
import io.activator.infinicast.client.api.paths.taskObjects.*;
import io.activator.infinicast.client.api.paths.handler.messages.*;
import io.activator.infinicast.client.api.paths.handler.reminders.*;
import io.activator.infinicast.client.api.paths.handler.lists.*;
import io.activator.infinicast.client.api.paths.handler.objects.*;
import io.activator.infinicast.client.api.paths.handler.requests.*;
import io.activator.infinicast.client.impl.contexts.*;
import io.activator.infinicast.client.impl.helper.*;
import io.activator.infinicast.client.impl.query.*;
import io.activator.infinicast.client.impl.messaging.*;
import io.activator.infinicast.client.impl.pathAccess.*;
import io.activator.infinicast.client.impl.responder.*;
import io.activator.infinicast.client.impl.objectState.*;
import io.activator.infinicast.client.impl.messaging.receiver.*;
import io.activator.infinicast.client.impl.messaging.handlers.*;
import io.activator.infinicast.client.impl.messaging.sender.*;
import io.activator.infinicast.client.protocol.messages.*;
public class PathUtils {
    public static String infinicastInternStart = "/~IC/";
    public static String cleanup(String path) {
        while (path.contains("//")) {
            path = path.replace("//", "/");
        }
        if (!(path.startsWith("/"))) {
            path = ("/" + path);
        }
        if ((!(path.endsWith("/")) && !(path.endsWith("*")))) {
            path = (path + "/");
        }
        return path;
    }
    public static String getObjectListPath(String path) {
        path = PathUtils.cleanup(path);
        return PathUtils.getParentPath(path);
    }
    static void createWildcardSubElement(String str, List<String> splitted, int index, ArrayList<String> results) {
        if ((index == splitted.size())) {
            results.add((str + "/"));
        }
        else {
            PathUtils.createWildcardSubElement(((str + "/") + splitted.get(index)), splitted, (index + 1), results);
            PathUtils.createWildcardSubElement((str + "/*"), splitted, (index + 1), results);
        }
    }
    public static ArrayList<String> getWildCardedPaths(String path) {
        ArrayList<String> list = new ArrayList<String>();
        if (StringExtensions.IsNullOrEmpty(path)) {
            list.add("");
            return list;
        }
        path = PathUtils.cleanup(path);
        // for c# splitted returns also the last element as empty. This is a difference between java and c# - MAX: how about the overload with StringSplitOptions?
            path = StringExtensions.butLast(path);
        List<String> splitted = StringExtensions.splitAsPath(path);
        PathUtils.createWildcardSubElement("", splitted, 1, list);
        return list;
    }
    public static String combine(String path1, String path2) {
        return PathUtils.cleanup(((path1 + "/") + path2));
    }
    public static String combineId(String path, String id) {
        return PathUtils.combine(path, id);
    }
    public static String getParentPath(String path) {
        path = PathUtils.cleanup(path);
        // all paths will end with / !
            path = StringExtensions.butLast(path);
        if (path.contains("/")) {
            path = path.substring(0, (path.lastIndexOf("/") + 1));
            return path;
        }
        return "";
    }
    public static String getLastPathPart(String key) {
        String str = PathUtils.cleanup(key);
        if (str.endsWith("/")) {
            str = str.substring(0, (str.length() - 1));
        }
        if ((str.lastIndexOf("/") != -1)) {
            str = str.substring((str.lastIndexOf("/") + 1));
        }
        return str;
    }
    public static String getEndpointPath(String endpointId) {
        return (("/~endpoints/" + endpointId) + "/");
    }
    public static String endpointDisconnectedByRolePath(String role) {
        return PathUtils.infinicastIntern(("roleDc/" + role));
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
        path = PathUtils.cleanup(path);
        return StringUtils.GetStringPathEleByIdx(path, idx);
    }
    public static String pathToString(IPath path) {
        if ((path == null)) {
            return "";
        }
        else {
            return path.toString();
        }
    }
}
