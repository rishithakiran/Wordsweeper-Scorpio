package com.scorpio.test.integration.server;

import com.scorpio.server.controller.*;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.serverbase.xml.Message;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;
/**
 * Test cases for handling XML ExitGame request.
 * @author Josh
 *
 */
public class ExitGameTest {
    
	@Before
    public void reset(){
        GameManager.reset();
    }

    private XMLUtil xml = new XMLUtil();

    /**
     * Test for when last player of the game leaves.
     */
    @Test
    public void functionality_LastPlayer(){
        ConnectionController router = new ConnectionController();
        CreateGameRequestController cgr = new CreateGameRequestController();

        Player p1 = new Player("aPlayer", null);
        try {
            cgr.createGame(p1, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }
        
        Message msg = xml.createMessageFromFile("testxml/exitGameRequest.xml");
        router.process(new FakeClientState("a"), msg);

        Game g = GameManager.getInstance().findGameById("mygame");

        assert(g == null);

    }

    /**
     * Test when Managing user leaves.
     */
    @Test
    public void functionality_AdminLeaves(){
        ConnectionController router = new ConnectionController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        JoinGameRequestController jgr = new JoinGameRequestController();

        Player p1 = new Player("aPlayer", new FakeClientState("a"));
        Player p2 = new Player("bPlayer", new FakeClientState("b"));
        try {
            cgr.createGame(p1, "mygame", null);
            jgr.joinGame(p2, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }

        Message msg = xml.createMessageFromFile("testxml/exitGameRequest.xml");
        router.process(new FakeClientState("a"), msg);

        Game g = GameManager.getInstance().findGameById("mygame");

        assert(g.getPlayers().size() == 1);
        assert(g.getPlayers().get(0).getName().equals("bPlayer"));
        assert(g.getManagingPlayer().getName().equals("bPlayer"));
    }

    /**
     * Test for basic functionality of exit game.
     */
    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        JoinGameRequestController jgr = new JoinGameRequestController();

        Player p1 = new Player("bPlayer", new FakeClientState("a"));
        Player p2 = new Player("aPlayer", new FakeClientState("b"));
        Player p3 = new Player("cPlayer", new FakeClientState("c"));

        try {
            cgr.createGame(p1, "mygame", null);
            jgr.joinGame(p2, "mygame", null);
            jgr.joinGame(p3, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }

        Message msg = xml.createMessageFromFile("testxml/exitGameRequest.xml");
        router.process(new FakeClientState("s"), msg);

        Game g = GameManager.getInstance().findGameById("mygame");

        assert(g.getPlayers().size() == 2);
        assert(g.getManagingPlayer().getName().equals("bPlayer"));

        // Ensure that b and c got a new boardResponse notifying them that a left
        assert(((FakeClientState)p1.getClientState()).getLastMessage() != null);
        assert(((FakeClientState)p3.getClientState()).getLastMessage() != null);
    }

    @Test
    public void resiliency_NoGame(){
        ConnectionController router = new ConnectionController();

        Message msg = xml.createMessageFromFile("testxml/exitGameRequest.xml");
        Message out = router.process(null, msg);
 
        assert(!out.success());
    }
}