package io.infinicast.client.api;
import io.infinicast.client.api.paths.ErrorInfo;
import io.activator.infinicast.*;
import java.util.*;

@FunctionalInterface
public interface DRoleListHandler {
    void accept(ErrorInfo errorInfo, ArrayList<String> roles);
}
