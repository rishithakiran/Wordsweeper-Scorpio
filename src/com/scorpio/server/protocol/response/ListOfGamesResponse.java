package com.scorpio.server.protocol.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Game;

public class ListOfGamesResponse {
	private final String requestId;

	public ListOfGamesResponse(String requestId){
		this.requestId = requestId;
	}

	private List<String> getGameXmlObject() {

		Integer noOfGames = GameManager.getInstance().numberOfGames();
		HashMap<String, Game> games = GameManager.getInstance().games;
		ArrayList<String> gameBriefXmlObject = new ArrayList<String>();

		for (int i = 0; i < noOfGames; i++) {
			String playerXMLObject = String.format("<gameBrief gameId='%s' players='%d'/>", games.get(i).getId(),
					games.get(i).getPlayers().size());
			gameBriefXmlObject.add(playerXMLObject);
		}
		return gameBriefXmlObject;
	}

	private String getHeader() {

		String header = "<response id='" + requestId + "' success='true'>";
		String boardResponseHeader = String.format("<listGamesResponse");

		return header + boardResponseHeader;
	}
	
	private String getFooter() {
		 String boardResponseFooter = "</listGamesResponse>";
	        String footer = "</response>";

	        return boardResponseFooter + footer;
	}
	
	public String toXML(){
        String complete = this.getHeader();

		for (String s : this.getGameXmlObject()) {
			complete += s;
		}

		complete += this.getFooter();



        // We will return the appropriate data to the client
        return complete;
    }

}
