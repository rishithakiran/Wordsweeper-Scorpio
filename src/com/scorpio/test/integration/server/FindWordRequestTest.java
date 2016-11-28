package com.scorpio.test.integration.server;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.GameAccessController;
import com.scorpio.server.controller.GameActionController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class FindWordRequestTest {
    private XMLUtil xml = new XMLUtil();
    @Before
    public void reset(){
        GameManager.reset();
    }

    @Test
    public void functionality_Basic(){
        ConnectionController router = new ConnectionController();
        GameAccessController gacc = new GameAccessController();

        Player aPlayer = new Player("aPlayer", new FakeClientState("a"));
        Player bPlayer = new Player("bPlayer", new FakeClientState("b"));

        try {
            gacc.createGame(aPlayer, "mygame", null);
            gacc.joinGame(bPlayer, "mygame", null);
        }catch(WordSweeperException ex){
            fail();
        }

        Board b = new RandomBoard(7);
        String boardString =("R B C D E F G" +
                             "E I J K L M N" +
                             "C P Q R S T U" +
                             "O R D Y Z A B" +
                             "C D E F G H I" +
                             "J K L M N O P" +
                             "Q R S T U V W")
                .replaceAll("\\s+","");
        for(int x = 0; x < b.getSize(); x++){
            for(int y = 0; y < b.getSize(); y++){
                b.getTileAt(new Coordinate(x,y)).setContents(String.valueOf(boardString.charAt((7 * y) + x)));
            }
        }

        GameManager.getInstance().findGameById("mygame").setBoard(b);

        assert(aPlayer.getScore() == 0);

        // Set out test user to 0,0
        GameManager.getInstance().findGameById("mygame").getPlayer("aPlayer").setLocation(new Coordinate(0,0));
        Message msg = xml.createMessageFromFile("testxml/findWordRequest.xml");
        aPlayer.getClientState().sendMessage(router.process(aPlayer.getClientState(), msg));

        int score = aPlayer.getScore();
        assert(score == 8320);

        // Ensure that the board has been updated
        // We don't need to check all tiles, BoardTest does this more completely
        assert(b.getTileAt(new Coordinate(0, 0)).getContents().equals("C"));
        assert(b.getTileAt(new Coordinate(0, 1)).getContents().equals("J"));
        assert(b.getTileAt(new Coordinate(0, 2)).getContents().equals("Q"));

        // Ensure that both A and B got return messages and a got two: a findWordResponse
        // and a board response
        assert(((FakeClientState)aPlayer.getClientState()).getLastMessage() != null);
        assert(((FakeClientState)aPlayer.getClientState()).getMsgsReceived() == 2);

        assert(((FakeClientState)bPlayer.getClientState()).getLastMessage() != null);


    }
}
