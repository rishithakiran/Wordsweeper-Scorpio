package com.scorpio.test.integration.server;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.LockGameRequestController;
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
 * Test cases for handling XML Join game request.
 * @author Josh
 * @author Apoorva
 */
public class JoinGameTest {
    private XMLUtil xml = new XMLUtil();

    @Before
    public void reset(){
        GameManager.reset();
    }
    /**Test for basic functionality of Joining a game request*/
    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        try {
            cgr.createGame(new Player("testPlayer", new FakeClientState("a")), "somePlace","abc");
        }catch(WordSweeperException ex){
            fail();
        }

        Game g = GameManager.getInstance().findGameById("somePlace");
        int playersBefore = g.getPlayers().size();
        Message msg = xml.createMessageFromFile("testxml/joinGameRequest.xml");
        router.process(new FakeClientState("b"), msg);
        int playersAfter = g.getPlayers().size();

        assert(playersBefore + 1 == playersAfter);
        assert(g.getPlayers().get(1).getName().equals("nextOne"));
    }

    /**
     * Ensure that we get an error when failing to join a game because it is locked
     */
    @Test
    public void functionality_HandleLocked(){
        ConnectionController router = new ConnectionController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        LockGameRequestController lgr = new LockGameRequestController();
        try {
            cgr.createGame(new Player("testPlayer", new FakeClientState("a")), "somePlace","abc");
            lgr.lockGame("somePlace");

        }catch(WordSweeperException ex){
            fail();
        }

        Game g = GameManager.getInstance().findGameById("somePlace");
        int playersBefore = g.getPlayers().size();
        Message msg = xml.createMessageFromFile("testxml/joinGameRequest.xml");
        String res = router.process(new FakeClientState("b"), msg).toString();
        int playersAfter = g.getPlayers().size();

        assert(playersBefore == playersAfter);
        assert(g.getPlayers().get(0).getName().equals("testPlayer"));

        String correctRes = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<response id=\"b\" reason=\"Game is locked!\" success=\"false\" version=\"1.0\">" +
                "<joinGameResponse gameId=\"somePlace\"/>" +
                "</response>";

        assert(res.equals(correctRes));

    }
}
