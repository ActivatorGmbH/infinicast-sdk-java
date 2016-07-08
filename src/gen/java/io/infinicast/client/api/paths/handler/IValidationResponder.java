package io.infinicast.client.api.paths.handler;
import io.infinicast.JObject;
import io.infinicast.*;

public interface IValidationResponder {
    void accept();
    void correct(JObject json);
    void reject();
}
