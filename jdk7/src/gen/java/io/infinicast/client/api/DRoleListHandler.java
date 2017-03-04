package io.infinicast.client.api;

import io.infinicast.FunctionalInterface;
import io.infinicast.client.api.errors.ICError;

import java.util.ArrayList;
@FunctionalInterface
public interface DRoleListHandler {
    void accept(ICError icError, ArrayList<String> roles);
}
