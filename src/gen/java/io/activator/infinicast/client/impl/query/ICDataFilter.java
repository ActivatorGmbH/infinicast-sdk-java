package io.activator.infinicast.client.impl.query;
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
public class ICDataFilter {
    int _dataFilterType;
    JObject _content;
    public static ICDataFilter fromJObject(JObject query) {
        ICDataFilter filter = new ICDataFilter();
        filter.setContent(query);
        filter.setDataFilterType(1);
        return filter;
    }
    public static ICDataFilter fromFieldFilter(Filter ff) {
        ICDataFilter filter = new ICDataFilter();
        filter.setContent(ff.toJson());
        filter.setDataFilterType(2);
        return filter;
    }
    public JObject toJson() {
        JObject json = new JObject();
        json.set("type", this.getDataFilterType());
        json.set("content", this.getContent());
        return json;
    }
    public int getDataFilterType() {
        return this._dataFilterType;
    }
    public void setDataFilterType(int value) {
        this._dataFilterType = value;
    }
    public JObject getContent() {
        return this._content;
    }
    public void setContent(JObject value) {
        this._content = value;
    }
}
