package com.scorpio.server.controller;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.accessory.Session;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.*;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.serverbase.xml.Message;
import org.w3c.dom.Node;
/**
 * Module that handles Reposition board request.
 * @author Josh
 *
 */
public class RepositionBoardRequestController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();

        String playerName = child.getAttributes().getNamedItem("name").getNodeValue();
        String targetGame = child.getAttributes().getNamedItem("gameId").getNodeValue();
        String rowChange = child.getAttributes().getNamedItem("rowChange").getNodeValue();
        String colChange = child.getAttributes().getNamedItem("colChange").getNodeValue();

        // Ensure that this client state belongs to this game
        Session s = (Session) state.getData();
        if(s == null){
            BoardResponse br = new BoardResponse(playerName, targetGame, request.id(), false, "Who are you?");
            return new Message(br.toXML());
        }
        if(!(s.getPlayer().getName().equals(playerName) && s.getGame().getId().equals(targetGame))){
            BoardResponse br = new BoardResponse(playerName, targetGame, request.id(), false, "You're lying to me");
            return new Message(br.toXML());
        }


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


    /**
     * Move a player within this game by the given amounts. This function enforces illegal moves,
     * and will throw exception if the player tries to do something not allowed
     * @param player The player to move
     * @param gameID The ID of the game to move the player in
     * @param colChange Delta in column. Must be between -1 and 1
     * @param rowChange Delta in row. Must be between -1 and 1
     * @throws WordSweeperException If the player attempts to move illegally or off the board
     */
    public void repositionBoard(String player, String gameID, int colChange, int rowChange) throws WordSweeperException {
        Game g = GameManager.getInstance().findGameById(gameID);

        // Verify that the proposed change is in bounds
        int playerBoardSize = 4;
        Player p = g.getPlayer(player);
        Coordinate currentLoc = p.getLocation();

        // Check right and bottom edges of the board
        if(currentLoc.col + playerBoardSize + colChange > g.getBoard().getSize() + 1 ||
                currentLoc.row + playerBoardSize + rowChange > g.getBoard().getSize() + 1){
            throw new WordSweeperException("Player move out of bounds");
        }

        // Check top and left edges of the board
        if(currentLoc.col + colChange < 1 || currentLoc.row + rowChange < 1){
            throw new WordSweeperException("Player move out of bounds");
        }

        // Do the move
        p.setLocation(new Coordinate(currentLoc.col + colChange, currentLoc.row + rowChange));
    }
}