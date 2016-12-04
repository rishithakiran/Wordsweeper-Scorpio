package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.*;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

public class ShowGameStateRequestController implements IProtocolHandler{
    @Override
    public Message process(ClientState state, Message request) {
        System.out.println(request);
        Node child = request.contents.getFirstChild();

        String gameId = child.getAttributes().item(0).getNodeValue();
        BoardResponse boardResponse = new BoardResponse(null, gameId, request.id(), true);
        return new Message(boardResponse.toXML());

    }
}
