package io.infinicast.client.api.paths.handler;
import io.infinicast.JObject;
import io.activator.infinicast.*;

public interface IValidationResponder {
    void accept();
    void correct(JObject json);
    void reject();
}
