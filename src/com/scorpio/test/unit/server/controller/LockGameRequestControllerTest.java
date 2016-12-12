package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.JoinGameRequestController;
import com.scorpio.server.controller.LockGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.test.util.FakeClientState;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
/**
 * Test cases for Lock game request.
 * @author Rishitha
 *
 */
public class LockGameRequestControllerTest {
    @Before
    public void resetGameManager() {
        GameManager.reset();
    }

    /**
     * Ensure locking an invalid game is handled properly.
     * @throws WordSweeperException
     */
    @Test(expected = WordSweeperException.class)
    public void error_LockGameDoesNotExist() throws WordSweeperException {
        LockGameRequestController lgr = new LockGameRequestController();
        lgr.lockGame("foo");
    }

    /**
     * Ensure that already locked game is not locked again.
     * @throws WordSweeperException
     */
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

    /**
     * Ensure that lock game mechanism is handled correctly and 
     * the request is from the managing user.
     * @throws WordSweeperException
     */
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

    /**
     * Ensure that we cannot join a locked game
     * @throws WordSweeperException
     */
    @Test(expected = WordSweeperException.class)
    public void functionality_GameIsActuallyLocked() throws WordSweeperException{
        LockGameRequestController lgr = new LockGameRequestController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        JoinGameRequestController jgr = new JoinGameRequestController();

        Player owner = new Player("test", null);
        String gameId = "abc";
        try {
            cgr.createGame(owner, gameId, null);
            Game testGame = GameManager.getInstance().findGameById("abc");
            lgr.lockGame(gameId);
        }catch(WordSweeperException ex){
            fail();
        }

        jgr.joinGame(new Player("test2", new FakeClientState("b")), "abc", null);
    }
}
