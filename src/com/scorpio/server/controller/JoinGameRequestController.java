package com.scorpio.server.controller;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.*;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

import java.util.Random;
/**
 * Modules that handles Join Game request.
 * @author Josh
 *
 */
public class JoinGameRequestController implements IProtocolHandler{
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();

        // Find the game
        String targetGame = child.getAttributes().getNamedItem("gameId").getNodeValue();
        Player newPlayer = new Player(child.getAttributes().getNamedItem("name").getNodeValue(), state);
        String password = null;
        if (child.getAttributes().getNamedItem("password") != null) {
            password = child.getAttributes().getNamedItem("password").getNodeValue();
        }

        try {
            this.joinGame(newPlayer, targetGame, password);
        } catch (WordSweeperException ex) {
            // If this occurs, we could not join the game
            JoinGameResponse jgr = new JoinGameResponse(targetGame, state.id(), ex.toString());
            return new Message(jgr.toXML());
        }

        // Notify all players that the game has changed
        GameManager.getInstance().findGameById(targetGame).notifyEveryoneBut(newPlayer.getName());

        String requestID = state.id();
        BoardResponse br = new BoardResponse(newPlayer.getName(), targetGame, requestID, false);
        return new Message(br.toXML());
    }


    /**
     * Add the given player to the specified game, resizing the board if
     * necessary
     *
     * @param player
     *            Player to add to game
     * @param gameID
     *            Target game
     * @param password
     *            Password for the game
     */
    public void joinGame(Player player, String gameID, String password) throws WordSweeperException {
        Game game = GameManager.getInstance().findGameById(gameID);
        if (game == null) {
            throw new WordSweeperException("Game doesn't exist");
        }

        for(Player p : game.getPlayers()){
            if(p.getName().equals(player.getName())){
                throw new WordSweeperException("Player already in game");
            }
        }

        // Check if game has a password
        if ((game.getPassword() != null) && !(game.getPassword().equals(password))) {
            throw new WordSweeperException("Password for the game does not match");
        }
        // We may need to resize the board
        if ((16 * (game.getPlayers().size() + 1) > (3 * (game.getBoard().getSize() ^ 2)))) {
            game.getBoard().grow(game.getBoard().getSize() + 1);
        }

        // Select a random location for our new player
        int playerBoardSize = 4;
        synchronized (game) {
            int x = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
            int y = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
            player.setLocation(new Coordinate(x + 1, y + 1));
            // Add them to the board
            game.addPlayer(player);
        }
    }
}
