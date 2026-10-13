package io.infinicast.client.api.paths;

import io.infinicast.JObject;
import io.infinicast.client.api.IEndpoint;
import io.infinicast.client.api.IPath;
public interface IPathAndEndpointContext {
    IPath getPath();
    IEndpoint getEndpoint();
    JObject getEndpointData();
    /** Core-generated request trace from the server envelope, if present. */
    default String getCoreAuditTraceId() { return null; }
    /** Core-verified ingress peer IP for /acp/auth/, if available. */
    default String getCoreTrustedClientIp() { return null; }
    /** Provenance of getCoreTrustedClientIp(), supplied in the Core envelope. */
    default String getCoreTrustedIpSource() { return null; }
}
