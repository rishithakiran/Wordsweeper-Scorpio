package com.scorpio.server.controller;


import com.scorpio.server.core.ClientState;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.xml.Message;

public class GameManagementController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        return null;
    }
}
