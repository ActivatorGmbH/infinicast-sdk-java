package io.infinicast.client.api;

import io.infinicast.client.api.errors.ICError;

import java.util.ArrayList;

public interface DRoleListHandler {
    void accept(ICError icError, ArrayList<String> roles);
}
