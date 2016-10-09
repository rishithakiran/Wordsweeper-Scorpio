package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.model.GameManager;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.IShutdownHandler;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

/**
 * Responsible for directing inbound connections to the appropriate
 * controller
 */
public class ConnectionController implements IShutdownHandler {
    GameManager model = null;
    public ConnectionController(GameManager m){
        this.model = m;
    }

    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();
        String type = child.getLocalName();

        System.out.println (request);
        if (type.equals ("createGameRequest")) {
            System.out.println("Creating a game");
            return new GameAccessController(model).process(state, request);
        } else if (type.equals ("joinGameRequest")) {
            System.out.println("Joining a game");
            return new GameAccessController(model).process(state, request);
        }

        // unknown? no idea what to do
        System.err.println("Unable to handle message:" + request);
        return null;

    }

    @Override
    public void logout(ClientState state) {

    }
}
