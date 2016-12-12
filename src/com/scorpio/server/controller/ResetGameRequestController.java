package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.server.protocol.response.ResetGameResponse;
import com.scorpio.serverbase.xml.Message;
import org.w3c.dom.Node;
/**
 * Module that handles Reset game request.
 * @author Josh
 *
 */
public class ResetGameRequestController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();




        String targetGame = child.getAttributes().getNamedItem("gameId").getNodeValue();
        Game g = GameManager.getInstance().findGameById(targetGame);
        if(g == null){
            ResetGameResponse rgr = new ResetGameResponse(targetGame, state.id(), "Game does not exist");
            return new Message(rgr.toXML());
        }

        Player manager = g.getManagingPlayer();
        String managerId = manager.getClientState().id();
        if(!managerId.equals(state.id())){
            ResetGameResponse rgr = new ResetGameResponse(targetGame, state.id(), "You are not the manager!");
            return new Message(rgr.toXML());
        }

        try {
            resetGame(targetGame);
        }catch(WordSweeperException ex){
            ResetGameResponse rgr = new ResetGameResponse(targetGame, state.id(), ex.toString());
            return new Message(rgr.toXML());
        }

        // Notify all players that the game has changed
        GameManager.getInstance().findGameById(targetGame).notifyEveryoneBut(manager.getName());

        String requestID = state.id();
        BoardResponse br = new BoardResponse(manager.getName(), targetGame, requestID, false);
        return new Message(br.toXML());
    }


    /**
     * Given a gameID, validate that the game exists and then reset it
     * @param gameId The game to lock
     * @throws WordSweeperException If the game does not exist
     */
    public static void resetGame(String gameId) throws WordSweeperException{
        Game g = GameManager.getInstance().findGameById(gameId);
        if(g == null){
            throw new WordSweeperException("Game does not exist");
        }
        int boardSize = g.getBoard().getSize();
        Board b = new RandomBoard(boardSize);
        g.setBoard(b);
        // Mutate all players to have a zero score
        for(Player p : g.getPlayers()) {
            p.setScore(0);
        }
    }
}