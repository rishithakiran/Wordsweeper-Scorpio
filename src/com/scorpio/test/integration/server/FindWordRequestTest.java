package com.scorpio.test.integration.server;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.accessory.Session;
import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.JoinGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.model.Tile;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.serverbase.xml.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
/**
 * Test cases for handling XML FindWord request.
 * @author Saranya
 * @author Josh
 *
 */
public class FindWordRequestTest {
    private XMLUtil xml = new XMLUtil();
    @Before
    public void reset(){
        GameManager.reset();
    }

    /**
     * Test for basic functionality of Find Word.
     * For the given input word, it verifies the players score for valid word 
     * and provides a corresponding response for other players.
     */
    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        JoinGameRequestController jgr = new JoinGameRequestController();

        FakeClientState fcs = new FakeClientState("e3b52914-fa5a-45d1-9fcc-a38ca0e6fcae");
        Player aPlayer = new Player("aPlayer", fcs);
        Player bPlayer = new Player("bPlayer", new FakeClientState("b"));

        try {
            cgr.createGame(aPlayer, "mygame", null);
            jgr.joinGame(bPlayer, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }

        // If we don't craft this right, the controller will think we're a hacker
        fcs.setData(new Session(aPlayer, GameManager.getInstance().findGameById("mygame")));

        Board b = new RandomBoard(7);
        String boardString =("R B C D E F G" +
                             "E I J K L M N" +
                             "C P Q R S T U" +
                             "O R D Y Z A B" +
                             "C D E F G H I" +
                             "J K L M N O P" +
                             "Q R S T U V W")
                .replaceAll("\\s+","");
        for(int row = 0; row < b.getSize(); row++){
            for(int col = 0; col < b.getSize(); col++){
                b.getTileAt(new Coordinate(col+1,row+1)).setContents(String.valueOf(boardString.charAt((7 * row) + col)));
            }
        }

        GameManager.getInstance().findGameById("mygame").setBoard(b);

        assert(aPlayer.getScore() == 0);

        // Set out test user to 1,1 and move our other user out of the way
        GameManager.getInstance().findGameById("mygame").getPlayer("aPlayer").setLocation(new Coordinate(1,1));
        GameManager.getInstance().findGameById("mygame").getPlayer("bPlayer").setLocation(new Coordinate(4,1));

        Message msg = xml.createMessageFromFile("testxml/findWordRequest.xml");
        aPlayer.getClientState().sendMessage(router.process(aPlayer.getClientState(), msg));

        int score = aPlayer.getScore();
        assert(score == 8320);

        // Ensure that the board has been updated
        // We don't need to check all tiles, BoardTest does this more completely
        Tile t = b.getTileAt(new Coordinate(1,1));
        assert(b.getTileAt(new Coordinate(1, 1)).getContents().equals("C"));
        assert(b.getTileAt(new Coordinate(1, 2)).getContents().equals("J"));
        assert(b.getTileAt(new Coordinate(1, 3)).getContents().equals("Q"));

        // Ensure that both A and B got return messages and a got two: a findWordResponse
        // and a board response
        assert(((FakeClientState)aPlayer.getClientState()).getLastMessage() != null);
        assert(((FakeClientState)aPlayer.getClientState()).getMsgsReceived() == 2);

        assert(((FakeClientState)bPlayer.getClientState()).getLastMessage() != null);


    }
}
