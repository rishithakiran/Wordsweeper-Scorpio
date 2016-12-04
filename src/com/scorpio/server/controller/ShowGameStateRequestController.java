package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.*;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;
/**
 * Module that handles Show game state request.
 * @author Apoorva
 *
 */
public class ShowGameStateRequestController implements IProtocolHandler{
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();

        String gameId = child.getAttributes().getNamedItem("gameId").getNodeValue();
        BoardResponse boardResponse = new BoardResponse(null, gameId, request.id(), true);
        return new Message(boardResponse.toXML());

    }
}
