package com.scorpio.test.integration.server;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.RepositionBoardRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Player;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class RepositionBoardTest {
    private XMLUtil xml = new XMLUtil();
    @Before
    public void reset(){
        GameManager.reset();
    }

    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        RepositionBoardRequestController gact = new RepositionBoardRequestController();
        CreateGameRequestController cgr = new CreateGameRequestController();

        Player aPlayer = new Player("aPlayer", new FakeClientState("a"));
        try {
            cgr.createGame(aPlayer, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }

        // Set out test user to 0,0
        GameManager.getInstance().findGameById("mygame").getPlayer("aPlayer").setLocation(new Coordinate(1,1));
        Message msg = xml.createMessageFromFile("testxml/repositionBoardRequest.xml");
        router.process(new FakeClientState("a"), msg);
        Coordinate newLoc = GameManager.getInstance().findGameById("mygame").getPlayer("aPlayer").getLocation();


        if(!newLoc.equals(new Coordinate(2,1))){
            fail();
        }
    }
}
