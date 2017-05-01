package io.infinicast.client.impl.responder;
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
public class RequestResponseManager {
    static Logger _logger = LoggerFactory.getLogger(RequestResponseManager.class);
    public static int timeBetweenChecks = 5000;
    ArrayList<RequestResponder> _toBeRemoved;
    ConcurrentHashMap<RequestResponder, RequestResponder> _awaitingResponders = new ConcurrentHashMap<RequestResponder, RequestResponder>();
    public RequestResponseManager() {
        this._toBeRemoved = new ArrayList<RequestResponder>();
    }
    public void initChecker(IntervalChecker requestResponseChecker) {
        requestResponseChecker.StartChecker(() -> {
            this._check();
        }
        , RequestResponseManager.timeBetweenChecks);
    }
    void _check() {
        try {
            for (RequestResponder responder : this._awaitingResponders.keySet()) {
                if (responder.alreadyResponded()) {
                    this._toBeRemoved.add(responder);
                }
                else if (responder.answerTakesLong()) {
                    RequestResponseManager._logger.info(("responder takes long " + responder.toString()));
                    responder.sendHandlingStarted();
                }
            }
            for (RequestResponder requestResponder : this._toBeRemoved) {
                this._awaitingResponders.remove(requestResponder);
            }
            if ((this._toBeRemoved.size() > 0)) {
                this._toBeRemoved.clear();
            }
        }
        catch (Exception ex) {
            RequestResponseManager._logger.error("Exception in requestResponseManager _check", ex);
        }
    }
    public void addResponder(RequestResponder responder) {
        ConcurrentHashmapExtensions.getOrAdd(responder, responder, this._awaitingResponders);
    }
}
