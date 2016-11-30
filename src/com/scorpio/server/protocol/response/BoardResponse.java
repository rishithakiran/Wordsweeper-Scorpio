package com.scorpio.server.protocol.response;

import java.util.ArrayList;
import java.util.List;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

public class BoardResponse {
	private final String playerID;
	private final String gameID;
	private final String requestID;
	private final boolean isAdminResponse;
	private final String error;

	public BoardResponse(String playerId,String gameID, String requestID, boolean isAdminResponse) {
		 this.playerID = playerId;
		this.gameID = gameID;
		this.requestID = requestID;
		this.isAdminResponse = isAdminResponse;
		this.error = null;
	}

	public BoardResponse(String playerID, String gameID, String requestID, boolean isAdminResponse, String error) {
		   this.playerID = playerID;
		this.gameID = gameID;
		this.requestID = requestID;
		this.isAdminResponse = isAdminResponse;
		this.error = error;
	}

	private List<String> getPlayerXMLObjects() {
		Game g = GameManager.getInstance().findGameById(this.gameID);
		List<Player> players = g.getPlayers();
		ArrayList<String> playerXMLObjects = new ArrayList<String>();
		for (Player p : players) {
			Coordinate nc = new Coordinate(p.getLocation().col, p.getLocation().row);

			String playerXMLObject = String.format("<player name='%s' position='%s' board='%s' score='%d'/>",
					p.getName(), nc.toString(), g.getPlayerBoard(p.getName()).toString(), p.getScore());
			playerXMLObjects.add(playerXMLObject);
		}

		return playerXMLObjects;
	}

	private String getHeader() {
		Game g = GameManager.getInstance().findGameById(gameID);

		String managingUser = g.getManagingPlayer().getName();
		String header;
		if (this.error != null) {
			header = "<response id='" + requestID + "' success='false' reason='" + this.error + "'>";
		} else {
			header = "<response id='" + requestID + "' success='true'>";
		}
		String boardResponseHeader = String.format("<boardResponse gameId='%s' managingUser='%s' bonus='%s'", gameID,
				managingUser, g.getBonus().toString());
		if (this.isAdminResponse) {
			Board b = g.getBoard();
			String extraData = String.format(" size='%d' contents='%s'>", b.getSize(), b.toString());
			boardResponseHeader += extraData;
		} else {
			boardResponseHeader += ">";
		}

		return header + boardResponseHeader;
	}

	private String getFooter() {
		String boardResponseFooter = "</boardResponse>";
		String footer = "</response>";

		return boardResponseFooter + footer;
	}

	public String toXML() {
		String complete = this.getHeader();

		for (String s : this.getPlayerXMLObjects()) {
			complete += s;
		}

		complete += this.getFooter();

		// We will return the appropriate data to the client
		return complete;
	}

}
