package io.infinicast.client.api.query;
import org.joda.time.DateTime;
import io.infinicast.*;
import java.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
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
/**
 * Field filter class can be used to filter on fields as part of a ChildrenQuery
*/
public class Filter {
    JObject data = new JObject();
    public Filter() {
    }
    /**
     * begins a fluent definition for a Filter for the given {@code field}
     * @param field
     * @return the created Filter.
    */
    public static Filter field(String field) {
        Filter ff = new Filter();
        ff.data.set("field", field);
        return ff;
    }
    /**
     * inverses the result of the field filter. For example Filter.Field("online").Not().Eq(true) would return elements with false
     * @return
    */
    public Filter not() {
        this.data.set("not", true);
        return this;
    }
    /**
     * checks if the field and the passed {@code value} are equal
     * @param value the value to be compared
     * @return
    */
    public Filter eq(boolean value) {
        this.data.set("op", "eq");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field and the passed {@code value} are equal
     * @param value the value to be compared
     * @return
    */
    public Filter eq(String value) {
        this.data.set("op", "eq");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field and the passed {@code value} are equal
     * @param value the value to be compared
     * @return
    */
    public Filter eq(long value) {
        this.data.set("op", "eq");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field and the passed {@code value} are equal
     * @param value the value to be compared
     * @return
    */
    public Filter eq(int value) {
        this.data.set("op", "eq");
        this.data.set("value", value);
        return this;
    }
    /**
     * compares the field with the passed {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter eq(float value) {
        this.data.set("op", "eq");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field and the passed {@code value} are equal
     * @param value the value to be compared
     * @return
    */
    public Filter eq(double value) {
        this.data.set("op", "eq");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is greater then the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter gt(float value) {
        this.data.set("op", "gt");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is greater then the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter gt(double value) {
        this.data.set("op", "gt");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is greater then the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter gt(int value) {
        this.data.set("op", "gt");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is greater then the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter gt(long value) {
        this.data.set("op", "gt");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is less then the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter lt(float value) {
        this.data.set("op", "lt");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is less then the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter lt(double value) {
        this.data.set("op", "lt");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is less then the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter lt(int value) {
        this.data.set("op", "lt");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is less then the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter lt(long value) {
        this.data.set("op", "lt");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is a collection (array) that contains the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter collectionContains(String value) {
        this.data.set("op", "contains");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is a collection (array) that contains the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter collectionContains(int value) {
        this.data.set("op", "contains");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is a collection (array) that contains the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter collectionContains(long value) {
        this.data.set("op", "contains");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is a collection (array) that contains the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter collectionContains(float value) {
        this.data.set("op", "contains");
        this.data.set("value", value);
        return this;
    }
    /**
     * checks if the field is a collection (array) that contains the {@code value}
     * @param value the value to be compared
     * @return
    */
    public Filter collectionContains(double value) {
        this.data.set("op", "contains");
        this.data.set("value", value);
        return this;
    }
    /**
     * converts the Filter to a json object
     * @return
    */
    public JObject toJson() {
        return this.data;
    }
}
