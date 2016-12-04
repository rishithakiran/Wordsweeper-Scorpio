package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.ResetGameResponse;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

/**
 * Created by spooky on 12/4/16.
 */
public class ResetGameRequestController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();
        String type = child.getLocalName();

        String targetGame = child.getAttributes().getNamedItem("gameId").getNodeValue();
        try {
            resetGame(targetGame);
        }catch(WordSweeperException ex){
            ResetGameResponse rgr = new ResetGameResponse(targetGame, state.id(), ex.toString());
            return new Message(rgr.toXML());
        }

        // Notify all players of chnage to board state
        GameManager.getInstance().findGameById(targetGame).notifyPlayers();

        // Finally, send a resetGameResponse to the initiator
        ResetGameResponse rgr = new ResetGameResponse(targetGame, state.id());
        return new Message(rgr.toXML());
    }

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
