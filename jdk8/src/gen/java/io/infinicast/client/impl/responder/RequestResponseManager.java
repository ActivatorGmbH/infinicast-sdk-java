package io.infinicast.client.impl.responder;

import io.infinicast.ConcurrentHashmapExtensions;
import io.infinicast.IntervalChecker;
import io.infinicast.Logger;
import io.infinicast.LoggerFactory;
import io.infinicast.client.impl.pathAccess.RequestResponder;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
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
            this.doCheck();
        }
        catch (Exception ex) {
            RequestResponseManager._logger.error("Exception in requestResponseManager _check", ex);
        }
    }
    void doCheck() {
        for (RequestResponder responder : this._awaitingResponders.keySet()) {
            if (responder.alreadyResponded()) {
                this._toBeRemoved.add(responder);
            }
            else if (responder.answerTakesLong()) {
                RequestResponseManager._logger.info("responder takes long " + responder.toString());
                responder.sendHandlingStarted();
            }
        }
        for (RequestResponder requestResponder : this._toBeRemoved) {
            this._awaitingResponders.remove(requestResponder);
        }
        if (this._toBeRemoved.size() > 0) {
            this._toBeRemoved.clear();
        }
    }
    public void addResponder(RequestResponder responder) {
        ConcurrentHashmapExtensions.getOrAdd(this._awaitingResponders, responder, responder);
    }
}
