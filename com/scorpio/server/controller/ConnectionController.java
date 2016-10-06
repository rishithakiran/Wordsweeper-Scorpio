package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.IShutdownHandler;
import com.scorpio.xml.Message;

/**
 * Responsible for directing inbound connections to the appropriate
 * controller
 */
public class ConnectionController implements IShutdownHandler {
    private GameAccessController accessController = new GameAccessController();
    private GameManagementController managementController = new GameManagementController();
    private GameActionController actionController = new GameActionController();


    @Override
    public Message process(ClientState state, Message request) {
        /*
            Switch based on content of request
         */
        return null;
    }

    @Override
    public void logout(ClientState state) {

    }
}
