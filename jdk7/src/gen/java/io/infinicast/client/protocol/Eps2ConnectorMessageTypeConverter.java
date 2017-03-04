package io.infinicast.client.protocol;

import java.util.HashMap;
public class Eps2ConnectorMessageTypeConverter {
    static HashMap<String, Integer> _stringToInt;
    static HashMap<Integer, String> _intToString;
    public static void init() {
        if ((Eps2ConnectorMessageTypeConverter._stringToInt == null)) {
            Eps2ConnectorMessageTypeConverter._stringToInt = new HashMap<String, Integer>();
            Eps2ConnectorMessageTypeConverter._intToString = new HashMap<Integer, String>();
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.Request.toString(), 1);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.RequestResponse.toString(), 2);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.InitConnector.toString(), 3);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.SetObjectData.toString(), 6);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.IntroduceObject.toString(), 7);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.Message.toString(), 8);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.MessageValidate.toString(), 9);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.ListAdd.toString(), 11);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.ListRemove.toString(), 12);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.ListChange.toString(), 13);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.RegisterHandler.toString(), 15);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.CreateOrUpdateRole.toString(), 16);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.DestroyRole.toString(), 17);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.GetOrCreate.toString(), 20);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.ListeningEnded.toString(), 21);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.ListeningStarted.toString(), 22);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.DebugStatistics.toString(), 26);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.PathRoleSetup.toString(), 27);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.Reminder.toString(), 36);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.ListeningChanged.toString(), 41);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.ListenTerminate.toString(), 43);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.DataChangeValidate.toString(), 45);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.EndpointDisconnected.toString(), 47);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.DebugObserverMessage.toString(), 48);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.JsonQueryResult.toString(), 1001);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.CreateChildSuccess.toString(), 1002);
            Eps2ConnectorMessageTypeConverter._stringToInt.put(Eps2ConnectorMessageType.GetRoleForPathResult.toString(), 1003);
            Eps2ConnectorMessageTypeConverter._intToString.put(1, Eps2ConnectorMessageType.Request.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(2, Eps2ConnectorMessageType.RequestResponse.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(3, Eps2ConnectorMessageType.InitConnector.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(6, Eps2ConnectorMessageType.SetObjectData.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(7, Eps2ConnectorMessageType.IntroduceObject.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(8, Eps2ConnectorMessageType.Message.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(9, Eps2ConnectorMessageType.MessageValidate.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(11, Eps2ConnectorMessageType.ListAdd.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(12, Eps2ConnectorMessageType.ListRemove.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(13, Eps2ConnectorMessageType.ListChange.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(15, Eps2ConnectorMessageType.RegisterHandler.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(16, Eps2ConnectorMessageType.CreateOrUpdateRole.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(17, Eps2ConnectorMessageType.DestroyRole.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(20, Eps2ConnectorMessageType.GetOrCreate.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(21, Eps2ConnectorMessageType.ListeningEnded.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(22, Eps2ConnectorMessageType.ListeningStarted.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(26, Eps2ConnectorMessageType.DebugStatistics.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(27, Eps2ConnectorMessageType.PathRoleSetup.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(36, Eps2ConnectorMessageType.Reminder.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(41, Eps2ConnectorMessageType.ListeningChanged.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(43, Eps2ConnectorMessageType.ListenTerminate.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(45, Eps2ConnectorMessageType.DataChangeValidate.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(47, Eps2ConnectorMessageType.EndpointDisconnected.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(48, Eps2ConnectorMessageType.DebugObserverMessage.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(1001, Eps2ConnectorMessageType.JsonQueryResult.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(1002, Eps2ConnectorMessageType.CreateChildSuccess.toString());
            Eps2ConnectorMessageTypeConverter._intToString.put(1003, Eps2ConnectorMessageType.GetRoleForPathResult.toString());
        }
    }
    public static int messageTypeToInt(Eps2ConnectorMessageType messageType) {
        return Eps2ConnectorMessageTypeConverter._stringToInt.get(messageType.toString());
    }
    public static Eps2ConnectorMessageType intToMessageType(int message) {
        return (Eps2ConnectorMessageType) Eps2ConnectorMessageType.valueOf(Eps2ConnectorMessageTypeConverter._intToString.get(message));
    }
}
