package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.model.GameManager;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.xml.Message;

public class GameAccessController implements IProtocolHandler{

    public GameAccessController(GameManager m){

    }

    @Override
    public Message process(ClientState state, Message request) {
        return null;
    }
}
