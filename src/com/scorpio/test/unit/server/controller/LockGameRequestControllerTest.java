package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.LockGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class LockGameRequestControllerTest {
    @Before
    public void resetGameManager() {
        GameManager.reset();
    }


    @Test(expected = WordSweeperException.class)
    public void error_LockGameDoesNotExist() throws WordSweeperException {
        LockGameRequestController lgr = new LockGameRequestController();
        lgr.lockGame("foo");

    }


    @Test(expected = WordSweeperException.class)
    public void error_GameAlreadyLocked() throws WordSweeperException{
        LockGameRequestController lgr = new LockGameRequestController();
        CreateGameRequestController cgr = new CreateGameRequestController();

        Player owner = new Player("test", null);
        String gameId = "abc";
        try {
            cgr.createGame(owner, gameId, null);
        }catch(WordSweeperException ex){
            fail();
        }
        Game testGame = GameManager.getInstance().findGameById("abc");
        testGame.setLocked(true);
        lgr.lockGame(gameId);
    }


    @Test
    public void functionality_LockGame() throws WordSweeperException{
        LockGameRequestController lgr = new LockGameRequestController();
        CreateGameRequestController cgr = new CreateGameRequestController();

        Player owner = new Player("test", null);
        String gameId = "abc";
        try {
            cgr.createGame(owner, gameId, null);
        }catch(WordSweeperException ex){
            fail();
        }
        Game testGame = GameManager.getInstance().findGameById("abc");
        lgr.lockGame(gameId);

        assert(testGame.isLocked());
    }
}
