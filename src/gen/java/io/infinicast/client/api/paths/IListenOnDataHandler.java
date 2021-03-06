package io.infinicast.client.api.paths;

import io.infinicast.JObject;
import io.infinicast.client.api.IPath;
public interface IListenOnDataHandler {
    void onAdd(JObject data, IPath context);
    void onChange(JObject data, IPath context);
    void onRemove(JObject data, IPath context);
}
