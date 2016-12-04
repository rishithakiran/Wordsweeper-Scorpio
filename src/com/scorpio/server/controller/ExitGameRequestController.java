package com.scorpio.server.controller;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.*;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Random;

public class ExitGameRequestController implements IProtocolHandler{

    @Override
    public Message process(ClientState state, Message request) {
        System.out.println(request);
        Node child = request.contents.getFirstChild();

        // Find the game
        String targetGame = child.getAttributes().item(0).getNodeValue();
        String targetPlayer = child.getAttributes().item(1).getNodeValue();

        try {
            this.exitGame(targetPlayer, targetGame);
        } catch (WordSweeperException ex) {
            // If this occurs, we could not join the game
            ExitGameResponse egr = new ExitGameResponse(targetGame, request.id(), ex.toString());
            return new Message(egr.toXML());
        }

        // If the game still exists, notify all remaining players
        if (GameManager.getInstance().findGameById(targetGame) != null) {
            // Notify all players of chaage to board state
            GameManager.getInstance().findGameById(targetGame).notifyPlayers();
        }

        ExitGameResponse egr = new ExitGameResponse(targetGame, request.id());
        return new Message(egr.toXML());
    }


    public void exitGame(String playerId, String gameId) throws WordSweeperException {
        Game g;
        if ((g = GameManager.getInstance().findGameById(gameId)) == null) {
            throw new WordSweeperException("Game does not exist");
        }

        // There's only one player left, and we're deleting them
        // No need to delete the player, just delete the game
        ArrayList<Player> pl = g.getPlayers();
        if (pl.size() == 1 && pl.get(0).getName().equals(playerId)) {

            GameManager.getInstance().deleteGame(gameId);
            return;
        }

        // Delete this user from the game
        g.deletePlayer(playerId);

        // If that player was the managing user, select someone else at random
        if (g.getManagingPlayer() == null) {
            int randomIndex = (new Random()).nextInt(g.getPlayers().size());
            g.getPlayers().get(randomIndex).setManagingUser(true);
        }
    }
}
