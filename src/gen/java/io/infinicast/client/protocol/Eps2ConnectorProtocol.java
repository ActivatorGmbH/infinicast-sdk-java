package io.infinicast.client.protocol;
import io.infinicast.APlayStringMessage;
import io.infinicast.JArray;
import io.infinicast.JObject;
import io.infinicast.NotImplementedException;
import io.infinicast.client.protocol.messages.Eps2ConnectorMessage;
import io.infinicast.client.utils.PathUtils;
import io.infinicast.*;

public class Eps2ConnectorProtocol {
    public void decodeStringMessage(APlayStringMessage message, IEndpoint2ConnectorProtocolHandler handler) {
        Eps2ConnectorMessage ob = Eps2ConnectorMessage.parseString(message);
        this.handleMessage(handler, ob);
    }
    void handleMessage(IEndpoint2ConnectorProtocolHandler handler, Eps2ConnectorMessage ob) {
        Eps2ConnectorMessageType messageType = ob.getType();
        JObject endpointObject = null;
        if ((ob.getEndpointObject() != null)) {
            endpointObject = new JObject(ob.getEndpointObject());
        }
        JObject data = null;
        if ((ob.getData() != null)) {
            data = new JObject(ob.getData());
        }
        switch (messageType) {
            case InitConnector:
             {
                    handler.onInitConnector(data, endpointObject);
                    break;}
            
            case IntroduceObject:
             {
                    handler.onIntroduceObject(data, ob.getPath(), endpointObject);
                    break;}
            
            case Message:
             {
                    handler.onReceiveMessage(data, ob.getPath(), endpointObject);
                    break;}
            
            case MessageValidate:
             {
                    handler.onReceiveMessageValidate(data, ob.getPath(), endpointObject);
                    break;}
            
            case DataChangeValidate:
             {
                    handler.onReceiveDataChangeValidate(data, ob.getPath(), endpointObject);
                    break;}
            
            case Request:
             {
                    handler.onReceiveRequest(data, ob.getPath(), (int) ob.getRequestId(), endpointObject);
                    break;}
            
            case RequestResponse:
             {
                    handler.onReceiveRequestResponse(data, (int) ob.getRequestId(), endpointObject);
                    break;}
            
            case JsonQueryResult:
             {
                    handler.onReceiveJsonQueryResult(new JArray(ob.getList()), (int) ob.getFullCount(), (int) ob.getRequestId());
                    break;}
            
            case CreateChildSuccess:
             {
                    handler.onCreateChildSuccess(data, ob.getPath(), (int) ob.getRequestId());
                    break;}
            
            case SetObjectData:
             {
                    handler.onSetObjectData(data, ob.getPath(), endpointObject);
                    break;}
            
            case ListAdd:
             {
                    handler.onListAdd(data, PathUtils.getParentPath(ob.getPath()), ob.getPath(), endpointObject);
                    break;}
            
            case ListRemove:
             {
                    handler.onListRemove(data, PathUtils.getParentPath(ob.getPath()), ob.getPath(), endpointObject);
                    break;}
            
            case ListChange:
             {
                    handler.onListChange(data, PathUtils.getParentPath(ob.getPath()), ob.getPath(), endpointObject);
                    break;}
            
            case GetOrCreate:
             {
                    handler.onGetOrCreate(data, ob.getPath(), (int) ob.getRequestId(), (boolean) ob.getNewlyCreated());
                    break;}
            
            case PathRoleSetup:
             {
                    handler.onPathRoleSetup(data, (int) ob.getRequestId());
                    break;}
            
            case CreateOrUpdateRole:
             {
                    // this should be ob?
                    handler.onCreateOrUpdateRole(data, (int) ob.getRequestId());
                    break;}
            
            case DestroyRole:
             {
                    // this should be ob?
                    handler.onDestroyRole(data, (int) ob.getRequestId());
                    break;}
            
            case GetRoleForPathResult:
             {
                    // this should be ob?
                    handler.onGetRoleForPathResult(new JArray(ob.getList()), data, (int) ob.getRequestId());
                    break;}
            
            case ListeningStarted:
             {
                    handler.onListeningStarted(ob.getPath(), endpointObject, ob.getData());
                    break;}
            
            case ListeningEnded:
             {
                    handler.onListeningEnded(ob.getPath(), endpointObject, (boolean) ob.getDisconnected(), ob.getData());
                    break;}
            
            case EndpointDisconnected:
             {
                    handler.onEndpointDisconnected(ob.getPath(), endpointObject);
                    break;}
            
            case ListenTerminate:
             {
                    handler.onListenTerminate(ob.getData());
                    break;}
            
            case DebugObserverMessage:
             {
                    handler.onDebugObserverMessage(ob.getPath(), ob.getData());
                    break;}
            
            case ListeningChanged:
             {
                    handler.onListeningChanged(ob.getPath(), endpointObject, ob.getData());
                    break;}
            
            case Reminder:
             {
                    handler.onReminderTriggered(ob.getPath(), ob.getData());
                    break;}
            
            case DebugStatistics:
             {
                    handler.onDebugStatistics(ob.getData(), (int) ob.getRequestId());
                    break;}
            
            default:
                throw new NotImplementedException(("not yet implemented messageType " + messageType));
            
        }
    }
}
