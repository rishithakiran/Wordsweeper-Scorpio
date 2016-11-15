package com.scorpio.serverbase;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.protocol.IProtocolHandler;

import java.io.*;
import java.net.*;
import java.util.*;

/** Generic Server in a Client/Server communication. */
public class Server {
	ServerSocket serverSocket = null;    /* ServerSocket to which we bind */
	int state = 0;                       /* Server state. 0=inactive, 1=accepting */
	IProtocolHandler protocolHandler;    /* Handler for protocol */
	final int serverPort;                /* Default server port. */
	
	/** Hold onto references to all threads actively managed by server. */
	static Hashtable<String, ClientState> ids = new Hashtable<String, ClientState>();

	public Server(IProtocolHandler ph, int port) {
		this.protocolHandler = ph;
		serverPort = port;
	}

	/** Bind Server to default port. */
	public void bind() throws IOException {
		serverSocket = new ServerSocket(serverPort);
		state = 1; 
	}

	/**
	 * Execute main server loop which receives client connection requests
	 * and spawns threads to execute each one. Process will handle all
	 * requests while state is 1 (accepting). Once no longer
	 * accepting requests, this method will shutdown the server. 
	 */
	public void process() throws IOException {
		while (state == 1) {
			Socket client = serverSocket.accept();

			new ServerThread(this, client, protocolHandler).start();
		} 

		shutdown();
	}

	/** Shutdown the server. */
	public void shutdown() throws IOException {
		if (serverSocket != null) {
			serverSocket.close();
			serverSocket = null;
			state = 0;
		}
	}

	/**
	 * Register thread in server.
	 * @param id
	 * @param thread
	 */
	public static boolean register (String id, ClientState state) {
		if (ids.containsKey(id)) { return false; }

		ids.put (id, state);
		return true;
	}

	public static void unregister (String id) {
		ids.remove(id); 
	}

	/**
	 * Return the clientState associated with this ID.
	 * 
	 * @param id
	 */
	public static ClientState getState(String id) {
		return ids.get(id);
	}

	/**
	 * Return the unique IDs for all connected clients.
	 * 
	 */
	public static Collection<String> ids() {
		return ids.keySet();
	} 
}
