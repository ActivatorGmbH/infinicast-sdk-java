package io.infinicast.client.api;

import io.infinicast.client.api.paths.ErrorInfo;

import java.util.ArrayList;
@FunctionalInterface
public interface DRoleListHandler {
    void accept(ErrorInfo errorInfo, ArrayList<String> roles);
}
