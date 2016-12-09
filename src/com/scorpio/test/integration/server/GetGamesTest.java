package com.scorpio.test.integration.server;

import org.junit.Test;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
/**
 * Test cases for handling XML List of games.
 * @author Apoorva
 *
 */
public class GetGamesTest {

	private XMLUtil xml = new XMLUtil();
	
	/**Test for basic functionality of List games.*/
	 @Test
	    public void functionality_Basic(){
	        ConnectionController router = new ConnectionController();
	        Message msg = xml.createMessageFromFile("testxml/listGamesRequest.xml");
	        router.process(null, msg);

	       /* int gamesBefore = GameManager.getInstance().numberOfGames();
	        router.process(null, msg);
	        int gamesAfter = GameManager.getInstance().numberOfGames();

	        assert(gamesBefore + 1 == gamesAfter);*/
	    }
}
