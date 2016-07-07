package io.activator.infinicast.client.api;
import org.joda.time.DateTime;
import io.activator.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import io.activator.infinicast.client.api.*;
import io.activator.infinicast.client.impl.*;
import io.activator.infinicast.client.utils.*;
import io.activator.infinicast.client.protocol.*;
import io.activator.infinicast.client.api.paths.*;
import io.activator.infinicast.client.api.query.*;
import io.activator.infinicast.client.api.paths.handler.*;
import io.activator.infinicast.client.api.paths.options.*;
import io.activator.infinicast.client.api.paths.taskObjects.*;
import io.activator.infinicast.client.api.paths.handler.messages.*;
import io.activator.infinicast.client.api.paths.handler.reminders.*;
import io.activator.infinicast.client.api.paths.handler.lists.*;
import io.activator.infinicast.client.api.paths.handler.objects.*;
import io.activator.infinicast.client.api.paths.handler.requests.*;
import io.activator.infinicast.client.impl.contexts.*;
import io.activator.infinicast.client.impl.helper.*;
import io.activator.infinicast.client.impl.query.*;
import io.activator.infinicast.client.impl.messaging.*;
import io.activator.infinicast.client.impl.pathAccess.*;
import io.activator.infinicast.client.impl.responder.*;
import io.activator.infinicast.client.impl.objectState.*;
import io.activator.infinicast.client.impl.messaging.receiver.*;
import io.activator.infinicast.client.impl.messaging.handlers.*;
import io.activator.infinicast.client.impl.messaging.sender.*;
import io.activator.infinicast.client.protocol.messages.*;
public class RoleSettings {
    Boolean _allowConnectAsRole;
    String _allowIps;
    String _allowEps;
    Boolean _grantDevSetup;
    String _grantRoleAssignments;
    Integer _timeoutInMs;
    public JObject toJson() {
        JObject result = new JObject();
        if ((this.getAllowConnectAsRole() != null)) {
            result.set("allowConnectAsRole", this.getAllowConnectAsRole());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getAllowEps()))) {
            result.set("allowEps", this.getAllowEps());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getAllowIps()))) {
            result.set("allowIps", this.getAllowIps());
        }
        if (!(StringExtensions.IsNullOrEmpty(this.getGrantRoleAssignments()))) {
            result.set("grantRoleAssignments", this.getGrantRoleAssignments());
        }
        if ((this.getGrantDevSetup() != null)) {
            result.set("grantDevSetup", this.getGrantDevSetup());
        }
        if ((this.getTimeoutInMs() != null)) {
            result.set("timeoutInMs", this.getTimeoutInMs());
        }
        return result;
    }
    public Boolean getAllowConnectAsRole() {
        return this._allowConnectAsRole;
    }
    public void setAllowConnectAsRole(Boolean value) {
        this._allowConnectAsRole = value;
    }
    public String getAllowIps() {
        return this._allowIps;
    }
    public void setAllowIps(String value) {
        this._allowIps = value;
    }
    public String getAllowEps() {
        return this._allowEps;
    }
    public void setAllowEps(String value) {
        this._allowEps = value;
    }
    public Boolean getGrantDevSetup() {
        return this._grantDevSetup;
    }
    public void setGrantDevSetup(Boolean value) {
        this._grantDevSetup = value;
    }
    public String getGrantRoleAssignments() {
        return this._grantRoleAssignments;
    }
    public void setGrantRoleAssignments(String value) {
        this._grantRoleAssignments = value;
    }
    public Integer getTimeoutInMs() {
        return this._timeoutInMs;
    }
    public void setTimeoutInMs(Integer value) {
        this._timeoutInMs = value;
    }
}
