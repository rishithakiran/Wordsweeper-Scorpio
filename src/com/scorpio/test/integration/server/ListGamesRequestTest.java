package com.scorpio.test.integration.server;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.JoinGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Player;
import com.scorpio.serverbase.xml.Message;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author Josh
 */
public class ListGamesRequestTest {
    private XMLUtil xml = new XMLUtil();

    @Before
    public void reset(){
        GameManager.reset();
    }

    @Test
    public void functionality_NoGames(){
        ConnectionController router = new ConnectionController();
        Message msg = xml.createMessageFromFile("testxml/listGamesRequest.xml");
        String res = router.process(new FakeClientState("a"), msg).toString();
        String correctResp = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response id=\"%s\" success=\"true\" version=\"1.0\">"
                        + "<listGamesResponse/></response>", "a");
        assert(res.equals(correctResp));

    }

    @Test
    public void functionality_1Game(){
        ConnectionController router = new ConnectionController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        try{
            cgr.createGame(new Player("b", new FakeClientState("b")), "testgame", null);
        }catch(WordSweeperException e){
            fail();
        }


        Message msg = xml.createMessageFromFile("testxml/listGamesRequest.xml");
        String res = router.process(new FakeClientState("a"), msg).toString();
        String correctResp = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response id=\"%s\" success=\"true\" version=\"1.0\"><listGamesResponse>" +
                "<gameBrief gameId=\"testgame\" players=\"1\"/>"+
                "</listGamesResponse></response>", "a");
        assert(res.equals(correctResp));

    }

    @Test
    public void functionality_2Game(){
        ConnectionController router = new ConnectionController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        try{
            cgr.createGame(new Player("b", new FakeClientState("b")), "testgame", null);
            cgr.createGame(new Player("c", new FakeClientState("c")), "testgame2", null);

        }catch(WordSweeperException e){
            fail();
        }


        Message msg = xml.createMessageFromFile("testxml/listGamesRequest.xml");
        String res = router.process(new FakeClientState("a"), msg).toString();
        String correctResp = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?><response id=\"%s\" success=\"true\" version=\"1.0\"><listGamesResponse>" +
                "<gameBrief gameId=\"testgame\" players=\"1\"/>"+
                "<gameBrief gameId=\"testgame2\" players=\"1\"/>"+
                "</listGamesResponse></response>", "a");
        assert(res.equals(correctResp));

    }


}
