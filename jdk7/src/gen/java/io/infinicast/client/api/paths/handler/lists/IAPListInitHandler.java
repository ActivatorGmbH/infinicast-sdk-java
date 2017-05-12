package io.infinicast.client.api.paths.handler.lists;

import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;

import java.util.ArrayList;
public interface IAPListInitHandler {
    void onInit(ArrayList<IPath> objekt, IEndpoint endpointInfo);
}
