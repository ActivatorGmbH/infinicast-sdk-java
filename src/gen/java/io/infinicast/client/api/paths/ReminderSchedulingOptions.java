package io.infinicast.client.api.paths;
import io.infinicast.JObject;
import org.joda.time.DateTime;
import io.activator.infinicast.*;

public class ReminderSchedulingOptions {
    DateTime dateTime;
    public ReminderSchedulingOptions withFixedDate(DateTime dateTime_) {
        this.dateTime = dateTime_;
        return this;
    }
    public JObject toJson() {
        JObject json = new JObject();
        json.set("fixed", this.dateTime);
        return json;
    }
}
