package com.scorpio.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;

import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.Server;
import com.scorpio.server.model.Tile;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.xml.Message;

public class GameAccessController implements IProtocolHandler {

	Server server;

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
		Server.games.put(game.getId(), game);
		return game.getBoard();
	}

	public Board joinGame(Player player, int gameId) {

		Game game = Server.games.get(gameId);
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
		if (Server.games.size() == 0) {
			return 1;
		}
		return Server.games.size() + 1;
		/*
		 * Random random = new Random(); // For now creating an id beween 1 to
		 * 100 return (random.nextInt((100 - 1) + 1) + 1);
		 */
	}
}
