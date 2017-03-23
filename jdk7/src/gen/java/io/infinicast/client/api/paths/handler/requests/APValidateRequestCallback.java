package io.infinicast.client.api.paths.handler.requests;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.handler.IValidationResponder;

public interface APValidateRequestCallback {
    void accept(JObject json, IValidationResponder validationResponder, IPath path, IEndpoint endpointInfo);
}
