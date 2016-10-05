package com.scorpio.server.protocol;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.IShutdownHandler;
import com.scorpio.server.core.Server;
import org.w3c.dom.Node;

import com.scorpio.xml.*;

/**
 * Sample implementation of a protocol handler to respond to messages received from clients.
 * You should follow this template when designing YOUR protocol handler.
 * <p>
 * Note that a new protocolHandler extension, {@link IShutdownHandler}, has been
 * developed and you should use that one moving forward.
 */
public class SampleProtocolHandler implements IShutdownHandler {

	/** Protocol handler knows the server in question. */
	Server server;

	/** Associate a server object with this handler. */
	public void setServer (Server s) {
		this.server = s;
	}

	public synchronized Message process (ClientState st, Message request) {
		Node child = request.contents.getFirstChild();
		
		if (child.getLocalName().equals ("connect")) {
			
		}

		// unknown? no idea what to do
		System.err.println("Unable to handle message:" + request);
		return null;
	}

	@Override
	public void logout(ClientState state) {
		System.err.println("Client is exiting: " + state.id());		
	} 
}
