package com.scorpio.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import com.scorpio.server.protocol.response.*;
import org.w3c.dom.Node;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.LockGameResponse;
import com.scorpio.xml.Message;

/**
 * 
 * @author Apoorva Controller to handle game access modules
 */
public class GameAccessController implements IProtocolHandler {

	@Override
	public Message process(ClientState state, Message request) {
		Node child = request.contents.getFirstChild();
		String type = child.getLocalName();
		switch (type) {
            case "createGameRequest": {
                String playerName = child.getAttributes().item(0).getNodeValue();
                String password = null;
                if (child.getAttributes().item(1) != null) {
                    password = child.getAttributes().item(1).getNodeName();
                }
                String gameUUID = UUID.randomUUID().toString();
                try {
                    this.createGame(new Player(playerName, state), gameUUID, password);
                    // Formulate a response and send it
                    BoardResponse br = new BoardResponse(playerName, gameUUID, request.id(), false);
                    return new Message(br.toXML());
                } catch (WordSweeperException ex) {
                    // If this occurs, we could not create a new game
                    FailureResponse fr = new FailureResponse(ex.toString(), request.id());
                    return new Message(fr.toXML());
                }
            }
            case "joinGameRequest": {
                // Find the game
                String targetGame = child.getAttributes().item(0).getNodeValue();
                Player newPlayer = new Player(child.getAttributes().item(1).getNodeValue(), state);
                String password = null;
                if (child.getAttributes().item(2) != null) {
                    password = child.getAttributes().item(2).getNodeValue();
                }

                try {
                    this.joinGame(newPlayer, targetGame, password);
                } catch (WordSweeperException ex) {
                    // If this occurs, we could not join the game
                    JoinGameResponse jgr = new JoinGameResponse(targetGame, state.id(), ex.toString());
                    return new Message(jgr.toXML());
                }

                // Notify all players that the game has changed
                List<Player> players = GameManager.getInstance().findGameById(targetGame).getPlayers();
                // Filter out the player that just joined the game (they'll get
                // their own request)
                players = players.stream().filter((s) -> !(s.getName().equals(newPlayer.getName())))
                        .collect(Collectors.toList());
                for (Player p : players) {
                    // If we've lost the client state, pass
                    if (p.getClientState() == null) {
                        continue;
                    }

                    String requestID = p.getClientState().id();
                    BoardResponse br = new BoardResponse(p.getName(), targetGame, requestID, false);
                    Message brm = new Message(br.toXML());
                    p.getClientState().sendMessage(brm);
                }

                // Finally, send the response to the player that just joined
                BoardResponse br = new BoardResponse(newPlayer.getName(), targetGame, request.id(), false);
                return new Message(br.toXML());
            }
            case "exitGameRequest": {
                // Find the game
                String targetGame = child.getAttributes().item(0).getNodeValue();
                String targetPlayer = child.getAttributes().item(1).getNodeValue();

                try {
                    this.exitGame(targetPlayer, targetGame);
                } catch (WordSweeperException ex) {
                    // If this occurs, we could not join the game
                    FailureResponse fr = new FailureResponse(ex.toString(), request.id());
                    return new Message(fr.toXML());
                }

                // If the game still exists, notify all remaining players
                if (GameManager.getInstance().findGameById(targetGame) != null) {
                    // Notify all players that the game has changed
                    List<Player> players = GameManager.getInstance().findGameById(targetGame).getPlayers();
                    // Filter out the player that just joined the game (they'll get
                    // their own request)
                    players = players.stream().filter((s) -> !(s.getName().equals(targetPlayer)))
                            .collect(Collectors.toList());
                    for (Player p : players) {
                        // If we've lost the client state, pass
                        if (p.getClientState() == null) {
                            continue;
                        }

                        String requestID = p.getClientState().id();
                        BoardResponse br = new BoardResponse(p.getName(), targetGame, requestID, false);
                        Message brm = new Message(br.toXML());
                        p.getClientState().sendMessage(brm);
                    }

                    // Finally, send the response to the player that just joined
                    BoardResponse br = new BoardResponse(targetPlayer, targetGame, request.id(), false);
                    return new Message(br.toXML());
                }
                return null;
            }
            case "lockGameRequest": {
                // Find the game
                String targetGame = child.getAttributes().item(0).getNodeValue();


                // Figure out if the request to lock the game came from the
                // the same ID as the one associated with the managing user
                String managingUserId = GameManager.getInstance()
                                            .findGameById(targetGame)
                                            .getManagingPlayer()
                                            .getClientState()
                                            .id();
                if(!managingUserId.equals(state.id())){
                    // You aren't the managing user! Ignored.
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
            case "listGamesRequest": {
                // this.getGames();
                ListOfGamesResponse listOfGamesResponse = new ListOfGamesResponse(request.id());
                return new Message(listOfGamesResponse.toXML());
            }

            default: {
                return null;
            }
		}
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
		// We need to verify this player is not already in the game
		if (game.getPlayers().contains(player)) {
			throw new WordSweeperException("Player already in game");
		}

		// Check if game has a password
		if ((game.getPassword() != null) && !(game.getPassword().equals(password))) {
			throw new WordSweeperException("Password for the game does not match");
		}
		// We may need to resize the board
		if ((16 * game.getPlayers().size() > (3 * (game.getBoard().getSize() ^ 2)))) {
			game.getBoard().grow(game.getBoard().getSize() + 1);
		}

		// Select a random location for our new player
		int playerBoardSize = 4;
		synchronized (game) {
			int x = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
			int y = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
			player.setLocation(new Coordinate(x, y));
			// Add them to the board
			game.addPlayer(player);
		}
	}

	/**
	 * Creates the game with appropriate values
	 * 
	 * @param player
	 * @param gameID
	 * @param password
	 * @throws WordSweeperException
	 */
	public void createGame(Player player, String gameID, String password) throws WordSweeperException {
		if (GameManager.getInstance().findGameById(gameID) != null) {
			throw new WordSweeperException("Game exists");
		}

		Game game = new Game();
		game.setBoard(new RandomBoard(7));
		game.setId(gameID);
		game.setLocked(false);
		if (password != null)
			game.setPassword(password);

		int playerBoardSize = 4;
		int x = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
		int y = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
		player.setLocation(new Coordinate(x, y));

		// server assigns player as a managing user and a participating user
		player.setManagingUser(true);
		game.addPlayer(player);

		GameManager.getInstance().games.put(game.getId(), game);
	}
	


	/**
	 * Locks the game with 
	 * 
	 * @param gameID Target game
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

