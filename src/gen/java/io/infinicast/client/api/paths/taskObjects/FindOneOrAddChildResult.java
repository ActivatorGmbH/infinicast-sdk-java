package io.infinicast.client.api.paths.taskObjects;

import io.infinicast.JObject;
import io.infinicast.client.api.paths.IAPathContext;
public class FindOneOrAddChildResult {
    public JObject data;
    public IAPathContext context;
    public boolean isNew;
}
