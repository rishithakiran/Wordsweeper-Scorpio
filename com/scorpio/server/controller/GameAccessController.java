package com.scorpio.server.controller;

import java.util.ArrayList;
import com.scorpio.server.core.ClientState;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.xml.Message;

public class GameAccessController implements IProtocolHandler {

	@Override
	public Message process(ClientState state, Message request) {
		return null;
	}

	/**
	 * Creates the game with appropriate values
	 * 
	 * @param player
	 */
	public Board createGame(Player player) {
		ArrayList<Player> players = new ArrayList<>();
		players.add(player);

		player.setManagingUser(true);

		Game game = new Game();
		game.setBoard(new Board());
		game.setId(generateId());
		game.setLocked(false);

		game.setPlayers(players);
		GameManager.getInstance().games.put(game.getId(), game);
		return game.getBoard();
	}

	public Board joinGame(Player player, int gameId) {

		Game game = GameManager.getInstance().games.get(gameId);
		// check if the game exists
		if ((game != null) && (!game.isLocked())) {
			if (game.getPassword() == null) {
				game.getPlayers().add(player);
				Board board = game.getBoard();
				return board.grow(board, 4);
			}

		}

		return null;

	}

	public void exitGame(Player player, int gameId) {

	}

	// How will you check if the random number is not repeated?
	private int generateId() {
		if (GameManager.getInstance().games.size() == 0) {
			return 1;
		}
		return GameManager.getInstance().games.size() + 1;
		/*
		 * Random random = new Random(); // For now creating an id beween 1 to
		 * 100 return (random.nextInt((100 - 1) + 1) + 1);
		 */
	}
}
