package com.scorpio.test.integration.server;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.GameAccessController;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Player;
import com.scorpio.serverbase.ServerThread;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.fail;


public class LockGameRequestTest {
    private XMLUtil xml = new XMLUtil();

    @Before
    public void reset(){
        GameManager.reset();
    }

    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        GameAccessController gac = new GameAccessController();
        ClientState s = new FakeClientState("foo");

        try {
            gac.createGame(new Player("testPlayer", s), "somePlace",null);
        }catch(WordSweeperException ex){
            fail();
        }

        Message msg = xml.createMessageFromFile("testxml/lockGameRequest.xml");

        router.process(s, msg);

        assert(GameManager.getInstance().findGameById("somePlace").isLocked());
    }

    @Test
    public void error_NotManagingUser(){
        ConnectionController router = new ConnectionController();
        GameAccessController gac = new GameAccessController();
        ClientState s = new FakeClientState("foo");

        try {
            gac.createGame(new Player("testPlayer", new FakeClientState("baz")), "somePlace",null);
        }catch(WordSweeperException ex){
            fail();
        }

        Message msg = xml.createMessageFromFile("testxml/lockGameRequest.xml");

        assert(!router.process(s, msg).success());

    }
    
    
    
}
