package io.infinicast.client.impl.responder;

import io.infinicast.ConcurrentHashmapExtensions;
import io.infinicast.IntervalChecker;
import io.infinicast.client.impl.pathAccess.RequestResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
public class RequestResponseManager {
    private static Logger _logger = LoggerFactory.getLogger(RequestResponseManager.class);
    public static int timeBetweenChecks = 5000;
    ConcurrentHashMap<RequestResponder, RequestResponder> _awaitingResponders = new ConcurrentHashMap<RequestResponder, RequestResponder>();
    public RequestResponseManager() {}
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
                this._awaitingResponders.remove(responder);
            }
            else if (responder.answerTakesLong()) {
                // Tracking is only needed until the one-time handling notice.
                // The request's completion callback still owns the responder and
                // may send its final answer after this entry is removed.
                try {
                    if (!responder.alreadyResponded()) {
                        RequestResponseManager._logger.info("responder takes long " + responder.toString());
                        responder.sendHandlingStarted();
                    }
                } finally {
                    this._awaitingResponders.remove(responder);
                }
            }
        }
    }

    public void clearAwaitingResponders() {
        this._awaitingResponders.clear();
    }
    public void addResponder(RequestResponder responder) {
        ConcurrentHashmapExtensions.getOrAdd(this._awaitingResponders, responder, responder);
    }
}
