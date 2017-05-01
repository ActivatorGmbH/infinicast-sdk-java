package io.infinicast.client.api.paths;
import io.infinicast.*;
import org.joda.time.DateTime;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import io.infinicast.client.api.*;
import io.infinicast.client.impl.*;
import io.infinicast.client.protocol.*;
import io.infinicast.client.utils.*;
import io.infinicast.client.api.errors.*;
import io.infinicast.client.api.paths.*;
import io.infinicast.client.api.query.*;
import io.infinicast.client.api.paths.handler.*;
import io.infinicast.client.api.paths.taskObjects.*;
import io.infinicast.client.api.paths.options.*;
import io.infinicast.client.api.paths.handler.messages.*;
import io.infinicast.client.api.paths.handler.reminders.*;
import io.infinicast.client.api.paths.handler.lists.*;
import io.infinicast.client.api.paths.handler.objects.*;
import io.infinicast.client.api.paths.handler.requests.*;
import io.infinicast.client.impl.contexts.*;
import io.infinicast.client.impl.helper.*;
import io.infinicast.client.impl.pathAccess.*;
import io.infinicast.client.impl.query.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
public class AtomicChange {
    ArrayList<AfinityNamedJsonDataQuery> namedQueryList = new ArrayList<AfinityNamedJsonDataQuery>();
    ArrayList<InternAtomicChange> atomicChangeList = new ArrayList<InternAtomicChange>();
    InternAtomicChange _removeProperty(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.RemoveProperty);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    InternAtomicChange _addToSet(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.AddToSet);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    InternAtomicChange _removeFromSet(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.RemoveFromSet);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    InternAtomicChange _addToArray(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.AddToArray);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    InternAtomicChange _removeFromArray(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.RemoveFromArray);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    InternAtomicChange _setValue(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.Set);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    InternAtomicChange _setValueIfEmpty(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.SetIfEmpty);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    InternAtomicChange _incValue(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.IncValue);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    InternAtomicChange _decValue(String jsonProperty) {
        InternAtomicChange change = new InternAtomicChange();
        change.setType(AtomicChangeType.DecValue);
        change.setJsonProperty(jsonProperty);
        this.atomicChangeList.add(change);
        return change;
    }
    public AtomicChange addToArray(String jsonProperty, JObject val) {
        this._addToArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToArray(String jsonProperty, JArray val) {
        this._addToArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToArray(String jsonProperty, String val) {
        this._addToArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToArray(String jsonProperty, double val) {
        this._addToArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToArray(String jsonProperty, int val) {
        this._addToArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange create() {
        return new AtomicChange();
    }
    public AtomicChange removeFromArray(String jsonProperty, JObject val) {
        this._removeFromArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromArray(String jsonProperty, JArray val) {
        this._removeFromArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromArray(String jsonProperty, String val) {
        this._removeFromArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromArray(String jsonProperty, float val) {
        this._removeFromArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromArray(String jsonProperty, double val) {
        this._removeFromArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromArray(String jsonProperty, int val) {
        this._removeFromArray(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToSet(String jsonProperty, JObject val) {
        this._addToSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToSet(String jsonProperty, JArray val) {
        this._addToSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToSet(String jsonProperty, String val) {
        this._addToSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToSet(String jsonProperty, float val) {
        this._addToSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToSet(String jsonProperty, double val) {
        this._addToSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange addToSet(String jsonProperty, int val) {
        this._addToSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromSet(String jsonProperty, JObject val) {
        this._removeFromSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromSet(String jsonProperty, JArray val) {
        this._removeFromSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromSet(String jsonProperty, String val) {
        this._removeFromSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromSet(String jsonProperty, float val) {
        this._removeFromSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromSet(String jsonProperty, double val) {
        this._removeFromSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeFromSet(String jsonProperty, int val) {
        this._removeFromSet(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange removeProperty(String jsonProperty) {
        this._removeProperty(jsonProperty);
        return this;
    }
    public AtomicChange setValue(String jsonProperty, JObject val) {
        this._setValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValue(String jsonProperty, JArray val) {
        this._setValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValue(String jsonProperty, String val) {
        this._setValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValue(String jsonProperty, boolean val) {
        this._setValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValue(String jsonProperty, double val) {
        this._setValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValue(String jsonProperty, int val) {
        this._setValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValueIfEmpty(String jsonProperty, JObject val) {
        this._setValueIfEmpty(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValueIfEmpty(String jsonProperty, JArray val) {
        this._setValueIfEmpty(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValueIfEmpty(String jsonProperty, String val) {
        this._setValueIfEmpty(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValueIfEmpty(String jsonProperty, boolean val) {
        this._setValueIfEmpty(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValueIfEmpty(String jsonProperty, double val) {
        this._setValueIfEmpty(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setValueIfEmpty(String jsonProperty, int val) {
        this._setValueIfEmpty(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange incValue(String jsonProperty, float val) {
        this._incValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange incValue(String jsonProperty, double val) {
        this._incValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange incValue(String jsonProperty, int val) {
        this._incValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange incValue(String jsonProperty, long val) {
        this._incValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange decValue(String jsonProperty, float val) {
        this._decValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange decValue(String jsonProperty, double val) {
        this._decValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange decValue(String jsonProperty, int val) {
        this._decValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange decValue(String jsonProperty, long val) {
        this._decValue(jsonProperty).setData(val);
        return this;
    }
    public AtomicChange setNamedCollectionQuery(String name, ICDataQuery query) {
        AfinityNamedJsonDataQuery named = new AfinityNamedJsonDataQuery();
        named.setName(name);
        named.setQuery(query);
        this.namedQueryList.add(named);
        return this;
    }
    public JArray toJson() {
        JArray arr = new JArray();
        for (InternAtomicChange change : this.atomicChangeList) {
            arr.Add(change.toJson());
        }
        return arr;
    }
    public JArray getNamedQueryJson() {
        JArray arr = new JArray();
        for (AfinityNamedJsonDataQuery named : this.namedQueryList) {
            JObject ob = new JObject();
            ob.set("name", named.getName());
            ob.set("query", named.getQuery().toJson());
            arr.Add(ob);
        }
        return arr;
    }
    public boolean hasNamedQueries() {
        return ((this.namedQueryList != null) && (this.namedQueryList.size() > 0));
    }
}
