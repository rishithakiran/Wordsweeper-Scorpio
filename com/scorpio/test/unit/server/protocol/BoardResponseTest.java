package com.scorpio.test.unit.server.protocol;

import com.scorpio.server.controller.GameAccessController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.response.BoardResponse;
import org.junit.Test;
import org.testng.annotations.BeforeTest;

import java.util.UUID;

public class BoardResponseTest {
    @BeforeTest
    public void resetGameManager(){
        GameManager.reset();
    }

    @Test
    public void functionality_Basic(){
        UUID gameUUID = UUID.randomUUID();
        UUID reqUUID = UUID.randomUUID();
        String testPlayerName = "testPlayer";
        GameAccessController gac = new GameAccessController();
        gac.createGame(new Player(testPlayerName, null), gameUUID.toString());
        Game g = GameManager.getInstance().findGameById(gameUUID.toString());

        String br = new BoardResponse(testPlayerName, gameUUID.toString(), reqUUID.toString(), false).toXML();
        String correctResp = String.format("<response id='%s' success='true'>" +
                "<boardResponse gameId='%s' managingUser='%s' bonus='0,0'>" +
                "<player name='%s' position='0,0' board='%s' score='0'/>" +
                "</boardResponse>" +
                "</response>", reqUUID.toString(), gameUUID.toString(), testPlayerName, testPlayerName, g.getPlayerBoard(testPlayerName));
        assert(br.equals(correctResp));
    }

    @Test
    public void functionality_Admin(){
        UUID gameUUID = UUID.randomUUID();
        UUID reqUUID = UUID.randomUUID();
        String testPlayerName = "testPlayer";
        GameAccessController gac = new GameAccessController();
        gac.createGame(new Player(testPlayerName, null), gameUUID.toString());
        Game g = GameManager.getInstance().findGameById(gameUUID.toString());

        String br = new BoardResponse(testPlayerName, gameUUID.toString(), reqUUID.toString(), true).toXML();
        String correctResp = String.format("<response id='%s' success='true'>" +
                "<boardResponse gameId='%s' managingUser='%s' bonus='0,0' size='%d' contents='%s'>" +
                "<player name='%s' position='0,0' board='%s' score='0'/>" +
                "</boardResponse>" +
                "</response>", reqUUID.toString(), gameUUID.toString(), testPlayerName, g.getBoard().getSize(), g.getBoard().toString(), testPlayerName, g.getPlayerBoard(testPlayerName));
        assert(br.equals(correctResp));
    }
}
