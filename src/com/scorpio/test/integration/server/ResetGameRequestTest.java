package com.scorpio.test.integration.server;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.GameAccessController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;


public class ResetGameRequestTest {
    private XMLUtil xml = new XMLUtil();

    @Before
    public void reset(){
        GameManager.reset();
    }

    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        GameAccessController gac = new GameAccessController();
        try {
            gac.createGame(new Player("testPlayer", null), "somePlace",null);
        }catch(WordSweeperException ex){
            fail();
        }
        Game g = GameManager.getInstance().findGameById("somePlace");
        g.getPlayer("testPlayer").setScore(20);
        Message msg = xml.createMessageFromFile("testxml/resetGameRequest.xml");
        router.process(new FakeClientState("foo"), msg);
        assert(g.getPlayer("testPlayer").getScore() == 0);
    }

    @Test
    public void resiliency_NoGame(){
        ConnectionController router = new ConnectionController();

        Message msg = xml.createMessageFromFile("testxml/resetGameRequest.xml");
        assert(!router.process(new FakeClientState("foo"), msg).success());
    }
}