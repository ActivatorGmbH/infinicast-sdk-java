package io.infinicast.client.api.paths.handler.reminders;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IPathAndEndpointContext;
@FunctionalInterface
public interface AReminderCallback {
    void accept(JObject json, IPathAndEndpointContext context);
}
