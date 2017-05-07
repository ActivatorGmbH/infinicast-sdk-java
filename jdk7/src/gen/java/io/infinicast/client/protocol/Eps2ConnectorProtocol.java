package io.infinicast.client.protocol;
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
public class Eps2ConnectorProtocol {
    public void decodeStringMessage(APlayStringMessage message, IEndpoint2ConnectorProtocolHandler handler) {
        Eps2ConnectorMessage ob = Eps2ConnectorMessage.parseString(message);
        this.handleMessage(handler, ob);
    }
    void handleMessage(IEndpoint2ConnectorProtocolHandler handler, Eps2ConnectorMessage ob) {
        Eps2ConnectorMessageType messageType = ob.getType();
        JObject endpointObject = null;
        if (ob.getEndpointObject() != null) {
            endpointObject = new JObject(ob.getEndpointObject());
        }
        JObject data = null;
        if (ob.getData() != null) {
            data = new JObject(ob.getData());
        }
        ICError err = ob.getError();
        switch (messageType) {
            case InitConnector:
             {
                    handler.onInitConnector(err, data, endpointObject);
                    break;}
            
            case IntroduceObject:
             {
                    handler.onIntroduceObject(err, data, ob.getPath(), endpointObject);
                    break;}
            
            case Message:
             {
                    handler.onReceiveMessage(err, data, ob.getPath(), endpointObject);
                    break;}
            
            case MessageValidate:
             {
                    handler.onReceiveMessageValidate(err, data, ob.getPath(), endpointObject);
                    break;}
            
            case DataChangeValidate:
             {
                    handler.onReceiveDataChangeValidate(err, data, ob.getPath(), endpointObject);
                    break;}
            
            case Request:
             {
                    handler.onReceiveRequest(err, data, ob.getPath(), (int) ob.getRequestId(), endpointObject);
                    break;}
            
            case RequestResponse:
             {
                    handler.onReceiveRequestResponse(err, data, (int) ob.getRequestId(), endpointObject);
                    break;}
            
            case JsonQueryResult:
             {
                    handler.onReceiveJsonQueryResult(err, new JArray(ob.getList()), (int) ob.getFullCount(), (int) ob.getRequestId());
                    break;}
            
            case CreateChildSuccess:
             {
                    handler.onCreateChildSuccess(err, data, ob.getPath(), (int) ob.getRequestId());
                    break;}
            
            case SetObjectData:
             {
                    handler.onSetObjectData(err, data, ob.getPath(), endpointObject);
                    break;}
            
            case ListAdd:
             {
                    handler.onListAdd(err, data, PathUtils.getParentPath(ob.getPath()), ob.getPath(), endpointObject);
                    break;}
            
            case ListRemove:
             {
                    handler.onListRemove(err, data, PathUtils.getParentPath(ob.getPath()), ob.getPath(), endpointObject);
                    break;}
            
            case ListChange:
             {
                    handler.onListChange(err, data, PathUtils.getParentPath(ob.getPath()), ob.getPath(), endpointObject);
                    break;}
            
            case GetOrCreate:
             {
                    handler.onGetOrCreate(err, data, ob.getPath(), (int) ob.getRequestId(), (boolean) ob.getNewlyCreated());
                    break;}
            
            case PathRoleSetup:
             {
                    handler.onPathRoleSetup(err, data, (int) ob.getRequestId());
                    break;}
            
            case CreateOrUpdateRole:
             {
                    // this should be ob?
                    handler.onCreateOrUpdateRole(err, data, (int) ob.getRequestId());
                    break;}
            
            case DestroyRole:
             {
                    // this should be ob?
                    handler.onDestroyRole(err, data, (int) ob.getRequestId());
                    break;}
            
            case GetRoleForPathResult:
             {
                    // this should be ob?
                    handler.onGetRoleForPathResult(err, new JArray(ob.getList()), data, (int) ob.getRequestId());
                    break;}
            
            case ListeningStarted:
             {
                    handler.onListeningStarted(err, ob.getPath(), endpointObject, ob.getData());
                    break;}
            
            case ListeningEnded:
             {
                    handler.onListeningEnded(err, ob.getPath(), endpointObject, (boolean) ob.getDisconnected(), ob.getData());
                    break;}
            
            case EndpointDisconnected:
             {
                    handler.onEndpointDisconnected(err, ob.getPath(), endpointObject);
                    break;}
            
            case ListenTerminate:
             {
                    handler.onListenTerminate(err, ob.getData());
                    break;}
            
            case DebugObserverMessage:
             {
                    handler.onDebugObserverMessage(err, ob.getPath(), ob.getData());
                    break;}
            
            case ListeningChanged:
             {
                    handler.onListeningChanged(err, ob.getPath(), endpointObject, ob.getData());
                    break;}
            
            case Reminder:
             {
                    handler.onReminderTriggered(err, ob.getPath(), ob.getData());
                    break;}
            
            case DebugStatistics:
             {
                    handler.onDebugStatistics(err, ob.getData(), (int) ob.getRequestId());
                    break;}
            
            default:
                throw new NotImplementedException("not yet implemented messageType " + messageType);
            
        }
    }
}
