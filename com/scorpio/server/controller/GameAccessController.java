package com.scorpio.server.controller;

import java.util.ArrayList;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

public class GameAccessController implements IProtocolHandler {
	GameManager gm;
	public GameAccessController(GameManager gm){
		this.gm = gm;
	}

	@Override
	public Message process(ClientState state, Message request) {
		Node child = request.contents.getFirstChild();
		String type = child.getLocalName();

		String playerName = child.getAttributes().item(0).getNodeValue();
		switch(type){
			case "createGameRequest":
				System.out.println(0);
				int id = createGame(new Player(playerName));
				Game g = GameManager.getInstance().findGameById(id);
				String managingUser = g.getManagingPlayer().getName();
				Coordinate coord = g.getBonus();
				String boardResponseHeader = String.format("<boardResponse gameId='%s' managingUser='%s' bonus='%s'>", id, managingUser, coord.toString());
				// One player for now
				String player = String.format("<player name='%s' position='%s' board='%s' score='%s'/>",
						playerName,
						g.getManagingPlayer().getLocation().toString(),
						g.getPlayerBoard(g.getManagingPlayer().getName()),
						"0");

				String boardResponseFooter = "</boardResponse>";
				String header = "<response id='" + request.id() + "' success='true'>";

				String footer = "</response>";

				String complete = header + boardResponseHeader + player + boardResponseFooter + footer;
				// We will return the apprpriate data to the client
				return new Message(complete);
		}
		return null;
	}

	/**
	 * Creates the game with appropriate values
	 * 
	 * @param player
	 */
	public int createGame(Player player) {
		ArrayList<Player> players = new ArrayList<>();
		player.setLocation(new Coordinate(0,0));
		players.add(player);

		player.setManagingUser(true);

		Game game = new Game();
		game.setBoard(new Board(7));
		game.getBoard().fillRandom();
		game.setId(generateId());
		game.setLocked(false);

		game.setPlayers(players);
		GameManager.getInstance().games.put(game.getId(), game);
		return game.getId();
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
