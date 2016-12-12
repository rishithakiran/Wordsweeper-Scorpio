package com.scorpio.server.protocol;

import com.scorpio.server.core.ClientState;
import com.scorpio.serverbase.xml.*;

/** 
 * ServerThread out-sources processing of protocol to implementors 
 * of this interface. Implementor is only concerned with request and response messages.
 */
public interface IProtocolHandler {

	/**
	 * Process the protocol given a Message request received on behalf of the designated 
	 * thread. Return Response Message as the response to the request or null to indicate
	 * that the connection to that client should be terminated.
	 * <p>
	 * ClientState object represents the client on whose behalf the request was received.
	 * Use this object to write back a response.
	 * <p>
	 * Classes that implement this interface must make sure that the
	 * {@link #process(ClientState, Message)} method is synchronized,
	 * otherwise 
	 */
	Message process(ClientState state, Message request);
}