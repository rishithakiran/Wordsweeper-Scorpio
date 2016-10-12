package com.scorpio.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

	private Message buildBoardResponse(String playerID, UUID gameID, String requestID){
		Game g = GameManager.getInstance().findGameById(gameID);

		String managingUser = g.getManagingPlayer().getName();
		String header = "<response id='" + requestID + "' success='true'>";
		String boardResponseHeader = String.format("<boardResponse gameId='%s' managingUser='%s' bonus='%s'>", gameID.toString(), managingUser, g.getBonus().toString());

		String playerXML = "";
		List<Player> players = g.getPlayers();
		for(Player p : players){
			playerXML += String.format("<player name='%s' position='%s' board='%s' score='%d'/>",
					playerID,
					p.getLocation().toString(),
					g.getPlayerBoard(p.getName()),
					p.getScore());
		}


		String boardResponseFooter = "</boardResponse>";
		String footer = "</response>";

		String complete = header + boardResponseHeader + playerXML + boardResponseFooter + footer;

		// We will return the appropriate data to the client
		return new Message(complete);
	}


	@Override
	public Message process(ClientState state, Message request) {
		Node child = request.contents.getFirstChild();
		String type = child.getLocalName();
		switch(type){
			case "createGameRequest":
				String playerName = child.getAttributes().item(0).getNodeValue();
				UUID gameID = createGame(new Player(playerName));
				return buildBoardResponse(playerName, gameID, request.id());

			case "joinGameRequest":
				// Find the game
				Game targetGame = GameManager.getInstance().findGameById(UUID.fromString(child.getAttributes().item(0).getNodeValue()));
				Player newPlayer = new Player(child.getAttributes().item(1).getNodeValue());

				// We may need to resize the board
				// TODO
				//targetGame.getBoard().grow(12);

				// Select a random location for our new player
				int x = (new Random()).nextInt(targetGame.getBoard().getSize()-3);
				int y = (new Random()).nextInt(targetGame.getBoard().getSize()-3);
				newPlayer.setLocation(new Coordinate(x, y));

				// Add them to the board
				targetGame.addPlayer(newPlayer);

				// Formulate a response and send it
				return buildBoardResponse(newPlayer.getName(), targetGame.getId(), request.id());

		}
		return null;
	}

	/**
	 * Creates the game with appropriate values
	 * 
	 * @param player
	 */
	public UUID createGame(Player player) {
		ArrayList<Player> players = new ArrayList<>();
		player.setLocation(new Coordinate(0,0));
		players.add(player);

		player.setManagingUser(true);

		Game game = new Game();
		game.setBoard(new Board(7));
		game.getBoard().fillRandom();
		game.setId(UUID.randomUUID());
		game.setLocked(false);

		game.setPlayers(players);
		GameManager.getInstance().games.put(game.getId(), game);
		return game.getId();
	}

	public void exitGame(Player player, int gameId) {

	}


}
