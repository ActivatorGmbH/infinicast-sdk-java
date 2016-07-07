package io.activator.infinicast.client.api.paths;
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
public class InternAtomicChange {
    public JObject json;
    public InternAtomicChange() {
        this.json = new JObject();
    }
    public void setData(JObject val) {
        this.json.set("data", val);
    }
    public void setData(JArray val) {
        this.json.set("data", val);
    }
    public void setData(String val) {
        this.json.set("data", val);
    }
    public void setData(int val) {
        this.json.set("data", val);
    }
    public void setData(long val) {
        this.json.set("data", val);
    }
    public void setData(float val) {
        this.json.set("data", val);
    }
    public void setData(double val) {
        this.json.set("data", val);
    }
    public void setData(boolean val) {
        this.json.set("data", val);
    }
    public JObject toJson() {
        return this.json;
    }
    public String getJsonProperty() {
        return this.json.getString("property");
    }
    public void setJsonProperty(String value) {
        this.json.set("property", value);
    }
    public AtomicChangeType getType() {
        return (AtomicChangeType) AtomicChangeType.valueOf(this.json.getString("type"));
    }
    public void setType(AtomicChangeType value) {
        this.json.set("type", value.toString());
    }
}
