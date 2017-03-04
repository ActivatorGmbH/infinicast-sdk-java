package io.infinicast.client.impl.query;

import io.infinicast.JArray;
import io.infinicast.JObject;
import io.infinicast.JToken;
import io.infinicast.QuadConsumer;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.errors.ICError;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.impl.pathAccess.IPathAndCount;
import io.infinicast.client.impl.pathAccess.PathAndCount;
import io.infinicast.client.protocol.Connector2EpsMessageType;

import java.util.ArrayList;
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
