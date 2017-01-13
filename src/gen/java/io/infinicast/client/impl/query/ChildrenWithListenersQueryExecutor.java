package io.infinicast.client.impl.query;

import io.infinicast.JArray;
import io.infinicast.JObject;
import io.infinicast.JToken;
import io.infinicast.TriConsumer;
import io.infinicast.client.api.IPath;
import io.infinicast.client.api.paths.ErrorInfo;
import io.infinicast.client.api.paths.IAPathContext;
import io.infinicast.client.impl.IConnector;
import io.infinicast.client.impl.messaging.ConnectorMessageManager;
import io.infinicast.client.protocol.Connector2EpsMessageType;

import java.util.ArrayList;
public class ChildrenWithListenersQueryExecutor extends BaseQueryExecutor  {
    public ChildrenWithListenersQueryExecutor(IConnector connector, IPath path, ConnectorMessageManager messageManager) {
        super(connector, path, messageManager);
    }
    public void getChildrenWithListeners(final TriConsumer<ErrorInfo, ArrayList<IPath>, IAPathContext> callback, int start, int limit) {
        JObject settings = new JObject();
        if (start >= 0) {
            settings.set("start", start);
        }
        if (limit >= 0) {
            settings.set("limit", limit);
        }
        super._messageManager.sendMessageWithResponse(Connector2EpsMessageType.GetMatchingPathsWithListeners, super._path, settings, (json, context) -> {
            if (!(super.checkIfHasErrorsAndCallHandlersNew(json, (error) -> {
                callback.accept(error, null, null);
                ;
            }))) {
                JArray array = json.getJArray("list");
                if (array != null) {
                    ArrayList<IPath> resultList = new ArrayList<IPath>();
                    for (JToken ob : array) {
                        String path = ob.toString();
                        resultList.add(super._connector.getObjectStateManager().getOrCreateLocalObject(path));
                    }
                    callback.accept(null, resultList, super.getPathContext(super._path));
                    ;
                }
                else {
                    throw new RuntimeException(new Exception("GetMatchingPathsWithListeners should always contain a list, even if it is empty"));
                }
            }
            ;
        });
    }
}
