package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.*;
import com.scorpio.serverbase.xml.Message;
import org.w3c.dom.Node;
/**
 * Module that handles Lock game request.
 * @author Rishitha
 *
 */
public class LockGameRequestController implements IProtocolHandler{
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();

        // Find the game
        String targetGame = child.getAttributes().getNamedItem("gameId").getNodeValue();

        // Figure out if the request to lock the game came from the
        // the same ID as the one associated with the managing user
        String managingUserId = GameManager.getInstance()
                .findGameById(targetGame)
                .getManagingPlayer()
                .getClientState()
                .id();

        if(!managingUserId.equals(state.id())){
            LockGameResponse lgr = new LockGameResponse(targetGame, request.id(), "Not managing user");
            return new Message(lgr.toXML());
        }

        try {
            this.lockGame(targetGame);
        } catch (WordSweeperException ex) {
            // If this occurs, we cannot not lock the game
            LockGameResponse lgr = new LockGameResponse(targetGame, request.id(), ex.toString());
            return new Message(lgr.toXML());
        }
        LockGameResponse lr = new LockGameResponse(targetGame, request.id());
        return new Message(lr.toXML());
    }


    /**
     * Attempt to lock the given game, checking that it exists, and can be locked
     *
     * @param gameID Target game
     * @throws WordSweeperException If the game does not exist or is already locked
     */
    public void lockGame(String gameID) throws WordSweeperException{
        Game game = GameManager.getInstance().findGameById(gameID);
        //String managingUser = game.getManagingPlayer().getName();
        //We need to verify this game exists
        if(game == null){
            throw new WordSweeperException("Game doesn't exist");
        }
        // We need to verify that this game is not locked already
        if(game.isLocked()==true){
            throw new WordSweeperException("Game already locked");
        }
        game.setLocked(true);
    }
}
