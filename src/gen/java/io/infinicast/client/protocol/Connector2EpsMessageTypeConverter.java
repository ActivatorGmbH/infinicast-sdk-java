package io.infinicast.client.protocol;

import java.util.HashMap;
public class Connector2EpsMessageTypeConverter {
    static HashMap<String, Integer> _stringToInt = new HashMap<String, Integer>();
    static HashMap<Integer, String> _intToString = new HashMap<Integer, String>();
    public static void init() {
        if (Connector2EpsMessageTypeConverter._stringToInt.size() == 0) {
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.Request.toString(), 1);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.RequestResponse.toString(), 2);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.InitConnector.toString(), 3);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.JsonQuery.toString(), 4);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.CreateChildRequest.toString(), 5);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.SetObjectData.toString(), 6);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.IntroduceObject.toString(), 7);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.Message.toString(), 8);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.MessageValidate.toString(), 9);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.MessageValidated.toString(), 10);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ListAdd.toString(), 11);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ListRemove.toString(), 12);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ListChange.toString(), 13);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.RemoveHandlers.toString(), 14);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.RegisterHandler.toString(), 15);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.CreateOrUpdateRole.toString(), 16);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DestroyRole.toString(), 17);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ModifyRoleForPath.toString(), 18);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetRoleForPath.toString(), 19);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetOrCreate.toString(), 20);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ListeningEnded.toString(), 21);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ListeningStarted.toString(), 22);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DeleteFromCollection.toString(), 23);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DebugPingInfo.toString(), 24);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DebugInfoMessage.toString(), 25);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DebugStatistics.toString(), 26);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.PathRoleSetup.toString(), 27);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.SetDebugName.toString(), 28);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetObjectData.toString(), 29);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetListeningList.toString(), 30);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.UpdateData.toString(), 31);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.SetChildData.toString(), 32);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ModifyChildData.toString(), 33);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.RemoveChildren.toString(), 34);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetEndpointConnectionInfo.toString(), 35);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.Reminder.toString(), 36);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.AddReminder.toString(), 37);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DeleteReminder.toString(), 38);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetAndListenOnChildren.toString(), 39);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DataListenUpdate.toString(), 40);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ListeningChanged.toString(), 41);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.SystemCommand.toString(), 42);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.ListenTerminate.toString(), 43);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetAndListenOnListeners.toString(), 44);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DataChangeValidate.toString(), 45);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DataChangeValidated.toString(), 46);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.EndpointDisconnected.toString(), 47);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.DebugObserverMessage.toString(), 48);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetEPSubscriptionList.toString(), 49);
            Connector2EpsMessageTypeConverter._stringToInt.put(Connector2EpsMessageType.GetMatchingPathsWithListeners.toString(), 50);
            Connector2EpsMessageTypeConverter._intToString.put(1, Connector2EpsMessageType.Request.toString());
            Connector2EpsMessageTypeConverter._intToString.put(2, Connector2EpsMessageType.RequestResponse.toString());
            Connector2EpsMessageTypeConverter._intToString.put(3, Connector2EpsMessageType.InitConnector.toString());
            Connector2EpsMessageTypeConverter._intToString.put(4, Connector2EpsMessageType.JsonQuery.toString());
            Connector2EpsMessageTypeConverter._intToString.put(5, Connector2EpsMessageType.CreateChildRequest.toString());
            Connector2EpsMessageTypeConverter._intToString.put(6, Connector2EpsMessageType.SetObjectData.toString());
            Connector2EpsMessageTypeConverter._intToString.put(7, Connector2EpsMessageType.IntroduceObject.toString());
            Connector2EpsMessageTypeConverter._intToString.put(8, Connector2EpsMessageType.Message.toString());
            Connector2EpsMessageTypeConverter._intToString.put(9, Connector2EpsMessageType.MessageValidate.toString());
            Connector2EpsMessageTypeConverter._intToString.put(10, Connector2EpsMessageType.MessageValidated.toString());
            Connector2EpsMessageTypeConverter._intToString.put(11, Connector2EpsMessageType.ListAdd.toString());
            Connector2EpsMessageTypeConverter._intToString.put(12, Connector2EpsMessageType.ListRemove.toString());
            Connector2EpsMessageTypeConverter._intToString.put(13, Connector2EpsMessageType.ListChange.toString());
            Connector2EpsMessageTypeConverter._intToString.put(14, Connector2EpsMessageType.RemoveHandlers.toString());
            Connector2EpsMessageTypeConverter._intToString.put(15, Connector2EpsMessageType.RegisterHandler.toString());
            Connector2EpsMessageTypeConverter._intToString.put(16, Connector2EpsMessageType.CreateOrUpdateRole.toString());
            Connector2EpsMessageTypeConverter._intToString.put(17, Connector2EpsMessageType.DestroyRole.toString());
            Connector2EpsMessageTypeConverter._intToString.put(18, Connector2EpsMessageType.ModifyRoleForPath.toString());
            Connector2EpsMessageTypeConverter._intToString.put(19, Connector2EpsMessageType.GetRoleForPath.toString());
            Connector2EpsMessageTypeConverter._intToString.put(20, Connector2EpsMessageType.GetOrCreate.toString());
            Connector2EpsMessageTypeConverter._intToString.put(21, Connector2EpsMessageType.ListeningEnded.toString());
            Connector2EpsMessageTypeConverter._intToString.put(22, Connector2EpsMessageType.ListeningStarted.toString());
            Connector2EpsMessageTypeConverter._intToString.put(23, Connector2EpsMessageType.DeleteFromCollection.toString());
            Connector2EpsMessageTypeConverter._intToString.put(24, Connector2EpsMessageType.DebugPingInfo.toString());
            Connector2EpsMessageTypeConverter._intToString.put(25, Connector2EpsMessageType.DebugInfoMessage.toString());
            Connector2EpsMessageTypeConverter._intToString.put(26, Connector2EpsMessageType.DebugStatistics.toString());
            Connector2EpsMessageTypeConverter._intToString.put(27, Connector2EpsMessageType.PathRoleSetup.toString());
            Connector2EpsMessageTypeConverter._intToString.put(28, Connector2EpsMessageType.SetDebugName.toString());
            Connector2EpsMessageTypeConverter._intToString.put(29, Connector2EpsMessageType.GetObjectData.toString());
            Connector2EpsMessageTypeConverter._intToString.put(30, Connector2EpsMessageType.GetListeningList.toString());
            Connector2EpsMessageTypeConverter._intToString.put(31, Connector2EpsMessageType.UpdateData.toString());
            Connector2EpsMessageTypeConverter._intToString.put(32, Connector2EpsMessageType.SetChildData.toString());
            Connector2EpsMessageTypeConverter._intToString.put(33, Connector2EpsMessageType.ModifyChildData.toString());
            Connector2EpsMessageTypeConverter._intToString.put(34, Connector2EpsMessageType.RemoveChildren.toString());
            Connector2EpsMessageTypeConverter._intToString.put(35, Connector2EpsMessageType.GetEndpointConnectionInfo.toString());
            Connector2EpsMessageTypeConverter._intToString.put(36, Connector2EpsMessageType.Reminder.toString());
            Connector2EpsMessageTypeConverter._intToString.put(37, Connector2EpsMessageType.AddReminder.toString());
            Connector2EpsMessageTypeConverter._intToString.put(38, Connector2EpsMessageType.DeleteReminder.toString());
            Connector2EpsMessageTypeConverter._intToString.put(39, Connector2EpsMessageType.GetAndListenOnChildren.toString());
            Connector2EpsMessageTypeConverter._intToString.put(40, Connector2EpsMessageType.DataListenUpdate.toString());
            Connector2EpsMessageTypeConverter._intToString.put(41, Connector2EpsMessageType.ListeningChanged.toString());
            Connector2EpsMessageTypeConverter._intToString.put(42, Connector2EpsMessageType.SystemCommand.toString());
            Connector2EpsMessageTypeConverter._intToString.put(43, Connector2EpsMessageType.ListenTerminate.toString());
            Connector2EpsMessageTypeConverter._intToString.put(44, Connector2EpsMessageType.GetAndListenOnListeners.toString());
            Connector2EpsMessageTypeConverter._intToString.put(45, Connector2EpsMessageType.DataChangeValidate.toString());
            Connector2EpsMessageTypeConverter._intToString.put(46, Connector2EpsMessageType.DataChangeValidated.toString());
            Connector2EpsMessageTypeConverter._intToString.put(47, Connector2EpsMessageType.EndpointDisconnected.toString());
            Connector2EpsMessageTypeConverter._intToString.put(48, Connector2EpsMessageType.DebugObserverMessage.toString());
            Connector2EpsMessageTypeConverter._intToString.put(49, Connector2EpsMessageType.GetEPSubscriptionList.toString());
            Connector2EpsMessageTypeConverter._intToString.put(50, Connector2EpsMessageType.GetMatchingPathsWithListeners.toString());
        }
    }
    public static int messageTypeToInt(Connector2EpsMessageType messageType) {
        return Connector2EpsMessageTypeConverter._stringToInt.get(messageType.toString());
    }
    public static Connector2EpsMessageType intToMessageType(int message) {
        return (Connector2EpsMessageType) Connector2EpsMessageType.valueOf(Connector2EpsMessageTypeConverter._intToString.get(message));
    }
}
