package com.scorpio.test.integration.server;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.core.GameManager;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
import org.junit.Test;

public class CreateGameTest {
    private XMLUtil xml = new XMLUtil();

    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        Message msg = xml.createMessageFromFile("testxml/createGameRequest.xml");

        int gamesBefore = GameManager.getInstance().numberOfGames();
        router.process(null, msg);
        int gamesAfter = GameManager.getInstance().numberOfGames();

        assert(gamesBefore + 1 == gamesAfter);
    }
    
    
    
}
