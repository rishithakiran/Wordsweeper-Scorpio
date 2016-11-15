package com.scorpio.test.integration.server;


import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.GameAccessController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;


public class ExitGameTest {
    @Before
    public void reset(){
        GameManager.reset();
    }

    private XMLUtil xml = new XMLUtil();

    @Test
    public void functionality_LastPlayer(){
        ConnectionController router = new ConnectionController();
        GameAccessController gac = new GameAccessController();

        Player p1 = new Player("aPlayer", null);
        try {
            gac.createGame(p1, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }


        Message msg = xml.createMessageFromFile("testxml/exitGameRequest.xml");
        router.process(null, msg);

        Game g = GameManager.getInstance().findGameById("mygame");

        assert(g == null);

    }

    @Test
    public void functionality_AdminLeaves(){
        ConnectionController router = new ConnectionController();
        GameAccessController gac = new GameAccessController();

        Player p1 = new Player("aPlayer", null);
        Player p2 = new Player("bPlayer", null);
        try {
            gac.createGame(p1, "mygame", null);
            gac.joinGame(p2, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }


        Message msg = xml.createMessageFromFile("testxml/exitGameRequest.xml");
        router.process(null, msg);

        Game g = GameManager.getInstance().findGameById("mygame");

        assert(g.getPlayers().size() == 1);
        assert(g.getPlayers().get(0).getName().equals("bPlayer"));
        assert(g.getManagingPlayer().getName().equals("bPlayer"));
    }

    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        GameAccessController gac = new GameAccessController();

        Player p1 = new Player("bPlayer", null);
        Player p2 = new Player("aPlayer", null);
        Player p3 = new Player("cPlayer", null);

        try {
            gac.createGame(p1, "mygame", null);
            gac.joinGame(p2, "mygame", null);
            gac.joinGame(p3, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }


        Message msg = xml.createMessageFromFile("testxml/exitGameRequest.xml");
        router.process(null, msg);

        Game g = GameManager.getInstance().findGameById("mygame");

        assert(g.getPlayers().size() == 2);
        assert(g.getManagingPlayer().getName().equals("bPlayer"));
    }

    @Test
    public void resiliency_NoGame(){
        ConnectionController router = new ConnectionController();

        Message msg = xml.createMessageFromFile("testxml/exitGameRequest.xml");
        Message out = router.process(null, msg);
        assert(!out.success());

    }
}