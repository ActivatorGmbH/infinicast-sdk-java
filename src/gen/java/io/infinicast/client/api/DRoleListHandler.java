package io.infinicast.client.api;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.*;
import java.util.*;

@FunctionalInterface
public interface DRoleListHandler {
    void accept(ErrorInfo errorInfo, ArrayList<String> roles);
}
