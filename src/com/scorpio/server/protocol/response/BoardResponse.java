package com.scorpio.server.protocol.response;

import java.util.ArrayList;
import java.util.List;
import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
/**
 * The Board Response holds all functionalities with respect to the corresponding 
 * requests those require a board response as a part of the process, necessary
 * to send to the client.
 * @author Josh
 *
 */
public class BoardResponse {
	private final String playerID;
	private final String gameID;
	private final String requestID;
	private final boolean isAdminResponse;
	private final String error;
	
	/**
	 * Constructor for BoardResponse which handles the playerID, gameId, requestId and Admin response.
	 * @param playerId			ID of the Player.
	 * @param gameID			ID of the Game.
	 * @param requestID			Request ID.
	 * @param isAdminResponse	Response from Admin.
	 */
	public BoardResponse(String playerId,String gameID, String requestID, boolean isAdminResponse) {
		this.playerID = playerId;
		this.gameID = gameID;
		this.requestID = requestID;
		this.isAdminResponse = isAdminResponse;
		this.error = null;
	}
	
	/**
	 * Constructor for BoardResponse which handles the playerID, gameId, requestId, error and Admin response.
	 * @param playerID			ID of the player.
	 * @param gameID			ID of the game.
	 * @param requestID			Request ID.
	 * @param isAdminResponse	Admin response.
	 * @param error				Error message.
	 */
	public BoardResponse(String playerID, String gameID, String requestID, boolean isAdminResponse, String error) {
		this.playerID = playerID;
		this.gameID = gameID;
		this.requestID = requestID;
		this.isAdminResponse = isAdminResponse;
		this.error = error;
	}

	/**
	 * Create response for XML header string for player objects.
	 * @return	Player object values.
	 */
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
	
	/**
	 * Create standard response XML header string for successful response or failure response.
	 * And create a board response XML header with gameId, managing user and bonus.
	 * @return	Header of the response.
	 */
	private String getHeader() {
		Game g = GameManager.getInstance().findGameById(gameID);
		String bonus;
		String managingUser;
		if(g == null){
			managingUser = "none";
			bonus = "0,0";
		}else{
			managingUser = g.getManagingPlayer().getName();
			bonus = g.getBonus().toString();
		}
		String header;
		if (this.error != null) {
			header = "<response id='" + requestID + "' success='false' reason='" + this.error + "'>";
		} else {
			header = "<response id='" + requestID + "' success='true'>";
		}

		String boardResponseHeader = String.format("<boardResponse gameId='%s' managingUser='%s' bonus='%s'", gameID,
				managingUser, bonus);
		if (this.isAdminResponse) {
			Board b = g.getBoard();
			String extraData = String.format(" size='%d' contents='%s'>", b.getSize(), b.toString());
			boardResponseHeader += extraData;
		} else {
			boardResponseHeader += ">";
		}

		return header + boardResponseHeader;
	}

	/**
	 * Create standard response XML footer string for Board response.
	 * @return	Footer of the response.
	 */
	private String getFooter() {
		String boardResponseFooter = "</boardResponse>";
		String footer = "</response>";

		return boardResponseFooter + footer;
	}

	/**
	 * The appropriate data is returned to the client.
	 * @return Header and Footer.
	 */
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
