package io.infinicast.client.api.paths;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;

import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
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
