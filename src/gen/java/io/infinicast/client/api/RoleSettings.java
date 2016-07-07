package io.infinicast.client.api;
import io.infinicast.JObject;
import io.infinicast.StringExtensions;
import io.activator.infinicast.*;

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
