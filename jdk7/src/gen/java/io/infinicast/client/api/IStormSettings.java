package io.infinicast.client.api;

import io.infinicast.CompletableFuture;
import io.infinicast.client.api.paths.options.CompleteCallback;
public interface IStormSettings {
    void createOrUpdateRole(String name, RoleSettings roleSettings);
    CompletableFuture<Void> createOrUpdateRoleAsync(String name, RoleSettings roleSettings);
    void createOrUpdateRole(String name, RoleSettings roleSettings, CompleteCallback completeCallback);
    CompletableFuture<Void> destroyRoleAsync(String name);
    void destroyRole(String name, CompleteCallback completeCallback);
    void destroyRole(String name);
}
