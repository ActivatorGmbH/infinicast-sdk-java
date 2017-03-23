package io.infinicast.client.impl.query;
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
import io.infinicast.client.impl.messaging.*;
import io.infinicast.client.impl.responder.*;
import io.infinicast.client.impl.objectState.*;
import io.infinicast.client.impl.messaging.handlers.*;
import io.infinicast.client.impl.messaging.receiver.*;
import io.infinicast.client.impl.messaging.sender.*;
import io.infinicast.client.protocol.messages.*;
public class ChildrenWithListenersQueryExecutor extends BaseQueryExecutor  {
    public ChildrenWithListenersQueryExecutor(IConnector connector, IPath path, ConnectorMessageManager messageManager) {
        super(connector, path, messageManager);
    }
    public void getChildrenWithListeners(final QuadConsumer<ICError, ArrayList<IPathAndCount>, IAPathContext, Integer> callback, int start, int limit) {
        JObject settings = new JObject();
        if ((start >= 0)) {
            settings.set("start", start);
        }
        if ((limit >= 0)) {
            settings.set("limit", limit);
        }
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetMatchingPathsWithListeners, super._path, settings, (json, err, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(err, (error) -> {
                callback.accept(error, null, null, 0);
                ;
            }
            ))) {
                JArray array = json.getJArray("list");
                if ((array != null)) {
                    ArrayList<IPathAndCount> resultList = new ArrayList<IPathAndCount>();
                    for (JToken ob : array) {
                        JObject job = (JObject) ob;
                        String path = job.getString("p");
                        int count = job.getInt("c");
                        PathAndCount ele = new PathAndCount();
                        ele.setCount(count);
                        ele.setPath(super._connector.getObjectStateManager().getOrCreateLocalObject(path));
                        resultList.add(ele);
                    }
                    callback.accept(null, resultList, super.getPathContext(super._path), json.getInt("count"));
                    ;
                }
                else {
                    throw new RuntimeException(new Exception("GetMatchingPathsWithListeners should always contain a list, even if it is empty"));
                }
            }
            ;
        }
        );
    }
}
