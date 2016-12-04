package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class CreateGameRequestControllerTest {
    @Before
    public void resetGameManager() {
        GameManager.reset();
    }

    @Test
    public void functionality_CreateGame() throws WordSweeperException {
        CreateGameRequestController cgr = new CreateGameRequestController();
        Player owner = new Player("test", null);
        String gameId = "abc";
        String password = "abc";
        cgr.createGame(owner, gameId, password);

        Game testGame = GameManager.getInstance().findGameById("abc");
        assert (testGame != null);
        assert (testGame.getManagingPlayer() == owner);
        assert (testGame.getPlayers().size() == 1);
        assert (testGame.getBoard().getSize() == 7);

    }


    @Test(expected = WordSweeperException.class)
    public void error_GameAlreadyExists() throws WordSweeperException {
        CreateGameRequestController cgr = new CreateGameRequestController();
        Player owner = new Player("test", null);
        String gameId = "abc";
        String password = "abc";
        try {
            cgr.createGame(owner, gameId, password);
        } catch (WordSweeperException ex) {
            fail();
        }

        cgr.createGame(new Player("test2", null), gameId, null);
    }


}
