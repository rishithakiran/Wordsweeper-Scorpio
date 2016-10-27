package com.scorpio.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.server.protocol.response.FailureResponse;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

public class GameAccessController implements IProtocolHandler {

	@Override
	public Message process(ClientState state, Message request) {

		Node child = request.contents.getFirstChild();
		String type = child.getLocalName();
		switch (type) {
		case "createGameRequest": {
			String playerName = child.getAttributes().item(0).getNodeValue();
			String gameUUID = UUID.randomUUID().toString();
			try {
				this.createGame(new Player(playerName, state), gameUUID);
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

			try {
				this.joinGame(newPlayer, targetGame);
			} catch (WordSweeperException ex) {
				// If this occurs, we could not join the game
				FailureResponse fr = new FailureResponse(ex.toString(), request.id());
				return new Message(fr.toXML());
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
	 */
	public void joinGame(Player player, String gameID) throws WordSweeperException {
		Game game = GameManager.getInstance().findGameById(gameID);
		if (game == null) {
			throw new WordSweeperException("Game doesn't exist");
		}
		// We need to verify this player is not already in the game
		if (game.getPlayers().contains(player)) {
			throw new WordSweeperException("Player already in game");
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
	 */
	public void createGame(Player player, String gameID) throws WordSweeperException {
		if (GameManager.getInstance().findGameById(gameID) != null) {
			throw new WordSweeperException("Game exists");
		}

		Game game = new Game();
		game.setBoard(new RandomBoard(7));
		game.setId(gameID);
		game.setLocked(false);

		int playerBoardSize = 4;
		int x = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
		int y = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
		player.setLocation(new Coordinate(x, y));

		// server assigns player as a managing user and a participating user
		player.setManagingUser(true);
		game.addPlayer(player);

		GameManager.getInstance().games.put(game.getId(), game);
	}

	public void exitGame(Player player, int gameId) {

	}

}