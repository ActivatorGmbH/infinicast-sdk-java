package io.infinicast.client.api.paths;
import io.infinicast.JArray;
import io.infinicast.JObject;
import io.activator.infinicast.*;

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
