package com.scorpio.server.protocol.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Game;
/**
 * The List of Game Response holds all functionalities with respect to the corresponding 
 * List of game requests, necessary to send to the client. 
 * @author Apoorva
 */
public class ListOfGamesResponse {
	private final String requestId;

	/**
	 * Constructor for List of game response that handles request ID.
	 * @param requestId	Request ID.
	 */
	public ListOfGamesResponse(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * Create response for XML header string for Game objects.
	 * @return
	 */
	private List<String> getGameXmlObject() {

		Integer noOfGames = GameManager.getInstance().numberOfGames();

		HashMap<String, Game> games = GameManager.getInstance().games;

		ArrayList<String> gameBriefXmlObject = new ArrayList<String>();
		for (String game : games.keySet()) {
			String playerXMLObject = String.format("<gameBrief gameId='%s' players='%d' />", game,
					games.get(game).getPlayers().size());
			gameBriefXmlObject.add(playerXMLObject);
		}

		return gameBriefXmlObject;
	}

	/**
	 * Create standard response XML header string for successful response.
	 * @return	Header of the response.
	 */
	private String getHeader() {
		String header = "<response id='" + requestId + "' success='true'>";
		String boardResponseHeader = String.format("<listGamesResponse>");
		
		return header + boardResponseHeader;
	}

	/**
	 * Create standard response XML footer string for ListGames response.
	 * @return	Footer of the response.
	 */
	private String getFooter() {
		String boardResponseFooter = "</listGamesResponse>";
		String footer = "</response>";

		return boardResponseFooter + footer;
	}

	/**
	 * The appropriate data is returned to the client.
	 * @return Header and Footer.
	 */
	public String toXML() {
		String complete = this.getHeader();
		for (String s : this.getGameXmlObject()) {
			complete += s;
		}
		complete += this.getFooter();

		// We will return the appropriate data to the client
		return complete;
	}
}
