package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.*;
import com.scorpio.xml.Message;

public class ListGamesRequestController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        ListOfGamesResponse listOfGamesResponse = new ListOfGamesResponse(request.id());
        return new Message(listOfGamesResponse.toXML());
    }
}
