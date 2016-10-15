package io.infinicast.client.api;


import io.infinicast.client.api.paths.options.*;

public interface IStormSettings {
    void createOrUpdateRole(String name, RoleSettings roleSettings);
    void createOrUpdateRole(String name, RoleSettings roleSettings, CompleteCallback completeCallback);
     void destroyRole(String name, CompleteCallback completeCallback);
    void destroyRole(String name);
}
