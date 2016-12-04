package com.scorpio.server.controller;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.*;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.server.protocol.response.FindWordResponse;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

import java.util.ArrayList;

/**
 * Created by spooky on 12/4/16.
 */
public class RepositionBoardRequestController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();

        String playerName = child.getAttributes().getNamedItem("name").getNodeValue();
        String targetGame = child.getAttributes().getNamedItem("gameId").getNodeValue();
        String rowChange = child.getAttributes().getNamedItem("rowChange").getNodeValue();
        String colChange = child.getAttributes().getNamedItem("colChange").getNodeValue();

        try{
            this.repositionBoard(playerName, targetGame, Integer.parseInt(colChange), Integer.parseInt(rowChange));
        }catch(WordSweeperException ex){
            BoardResponse br = new BoardResponse(playerName, targetGame, request.id(), false, ex.toString());
            return new Message(br.toXML());
        }

        GameManager.getInstance().findGameById(targetGame).notifyEveryoneBut(playerName);

        String requestID = state.id();
        BoardResponse br = new BoardResponse(playerName, targetGame, requestID, false);
        return new Message(br.toXML());
    }

    public void repositionBoard(String player, String gameID, int rowChange, int colChange) throws WordSweeperException {
        Game game = GameManager.getInstance().findGameById(gameID);

        // This may throw a WordSweeperException up the stack
        game.repositionPlayer(player, rowChange, colChange);
    }
}
