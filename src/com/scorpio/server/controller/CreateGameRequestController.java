package com.scorpio.server.controller;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.accessory.Session;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.*;
import com.scorpio.serverbase.xml.Message;
import org.w3c.dom.Node;
import java.util.Random;

/**
 * Module that handles creating game request.
 * @author Josh
 * @author Apoorva
 *
 */
public class CreateGameRequestController implements IProtocolHandler{
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();
        String playerName = child.getAttributes().getNamedItem("name").getNodeValue();
        String gameUUID = String.valueOf(GameManager.getInstance().getNextID());

        // Affiliate this client state with a session, if it doesn't already have one. If it does, they shouldn't
        // be allowed to join -- no multiboxers!
        Session s = (Session) state.getData();
        if(s != null){
            BoardResponse br = new BoardResponse(playerName, gameUUID, request.id(), false, "Please leave your other game first");
            return new Message(br.toXML());
        }




        String password = null;
        if (child.getAttributes().getNamedItem("password") != null) {
            password = child.getAttributes().getNamedItem("password").getNodeValue();
        }



        try {
            Player np = new Player(playerName, state);
            this.createGame(np, gameUUID, password);

            // Store the session
            state.setData((Object) new Session(np, GameManager.getInstance().findGameById(gameUUID)));

            // Formulate a response and send it
            BoardResponse br = new BoardResponse(playerName, gameUUID, request.id(), false);
            return new Message(br.toXML());
        } catch (WordSweeperException ex) {
            BoardResponse br = new BoardResponse(playerName, gameUUID, request.id(), false, "Could not create game");
            return new Message(br.toXML());
        }
    }


    /**
	 * Creates a new game with appropriate values.
	 * 
	 * @param 	player	Player to create a new game.
	 * @param	gameID	The ID generated create a new game.
	 * @param	password	Password for the game.
	 * @throws WordSweeperException	Throws appropriate Exception results.
	 */
    public void createGame(Player player, String gameID, String password) throws WordSweeperException {
        if (GameManager.getInstance().findGameById(gameID) != null) {
            throw new WordSweeperException("Game exists");
        }

        Game game = new Game();
        Board b = new RandomBoard(7);
        b.addBonus();
        game.setBoard(b);
        game.setId(gameID);
        game.setLocked(false);
        if (password != null) {
            game.setPassword(password);
        }
        int playerBoardSize = 4;
        int x = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
        int y = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
        player.setLocation(new Coordinate(x + 1, y + 1));

        // server assigns player as a managing user and a participating user
        player.setManagingUser(true);
        game.addPlayer(player);
        GameManager.getInstance().games.put(game.getId(), game);
    }
}
