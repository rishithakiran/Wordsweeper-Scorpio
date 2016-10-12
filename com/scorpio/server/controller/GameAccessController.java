package com.scorpio.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
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
			// for the purpose of testing, UUID is always 'somePlace'
			gameUUID = "somePlace";
			this.createGame(new Player(playerName, state), gameUUID);

			// Formulate a response and send it
			BoardResponse br = new BoardResponse(playerName, gameUUID, request.id(), false);
			return new Message(br.toXML());
		}
		case "joinGameRequest": {
			// Find the game
			Game targetGame = GameManager.getInstance().findGameById(child.getAttributes().item(0).getNodeValue());
			Player newPlayer = new Player(child.getAttributes().item(1).getNodeValue(), state);
			this.joinGame(newPlayer, targetGame);

			// Notify all players that the game has changed
			List<Player> players = targetGame.getPlayers();
			// Filter out the player that just joined the game (they'll get
			// their own request)
			players = players.stream().filter((s) -> !(s.getName().equals(newPlayer.getName())))
					.collect(Collectors.toList());
			for (Player p : players) {
				// Formulate BoardResponse and send it.
				String requestID = p.getClientState().id();
				BoardResponse br = new BoardResponse(p.getName(), targetGame.getId(), requestID, true);
				Message brm = new Message(br.toXML());
				p.getClientState().sendMessage(brm);
			}

			// Finally, send the response to the player that just joined
			BoardResponse br = new BoardResponse(newPlayer.getName(), targetGame.getId(), request.id(), true);
			return new Message(br.toXML());
		}
		}
		return null;
	}

	/**
	 * Add the given player to the specified game, resizing the board if
	 * necessary
	 * 
	 * @param player
	 *            Player to add to game
	 * @param game
	 *            Target game
	 */
	public void joinGame(Player player, Game game) {
		// We may need to resize the board
		// TODO
		// targetGame.getBoard().grow(12);

		// Select a random location for our new player
		if ((16 * game.getPlayers().size() > (3 * (game.getBoard().getSize() ^ 2)))) {
			game.getBoard().grow(game.getBoard().getSize() + 1);
		}
		int playerBoardSize = 4;
		int x = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
		int y = (new Random()).nextInt(game.getBoard().getSize() - playerBoardSize);
		player.setLocation(new Coordinate(x, y));

		// Add them to the board
		game.addPlayer(player);
	}

	/**
	 * Creates the game with appropriate values
	 * 
	 * @param player
	 */
	public void createGame(Player player, String gameID) {
		ArrayList<Player> players = new ArrayList<>();
		player.setLocation(new Coordinate(0, 0));
		players.add(player);

		player.setManagingUser(true);

		Game game = new Game();
		game.setBoard(new RandomBoard(7));
		game.setId(gameID);
		game.setLocked(false);

		game.setPlayers(players);
		GameManager.getInstance().games.put(game.getId(), game);
	}

	public void exitGame(Player player, int gameId) {

	}

}