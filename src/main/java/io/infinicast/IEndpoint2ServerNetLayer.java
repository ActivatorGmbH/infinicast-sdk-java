package io.infinicast;


import java.util.List;

public interface IEndpoint2ServerNetLayer {

	String open(IEndpoint2ServerNetSettings settings);
	void closeDirect(); // stops cloud system, no longer possible to receive or send any message
	void closeAndWait(); // stops the cloud system and waits for all messages to be sent before closing. It is directly not possible to receive other messages
	void Close();

	void SendToServer(APlayStringMessage message); // sends a guaranteed single message to the target server. If no connection is available yet, it tries to open a connection
	void SendToServer(List<APlayStringMessage> messages);  // sends a guaranteed ordered list of messages to the target server.  If no connection is available yet, it tries to open a connection

	IEndpointAddress getPublicAddress(); //return the endpoints that can be given to the server

}
