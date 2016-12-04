package com.scorpio.test.unit.server.controller;


import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.JoinGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class JoinGameRequestControllerTest {
    @Before
    public void resetGameManager() {
        GameManager.reset();
    }


    @Test(expected = WordSweeperException.class)
    public void error_JoinGameDoesNotExist() throws WordSweeperException {
        JoinGameRequestController jgr = new JoinGameRequestController();
        Player owner = new Player("test", null);
        String gameId = "abc";
        String password = "abc";
        jgr.joinGame(owner, gameId, password);
    }


    @Test
    public void functionality_JoinGame() throws WordSweeperException {
        JoinGameRequestController jgr = new JoinGameRequestController();
        CreateGameRequestController cgr = new CreateGameRequestController();

        Player owner = new Player("test", null);
        String gameId = "abc";
        String password = "abc";
        cgr.createGame(owner, gameId, null);

        Game testGame = GameManager.getInstance().findGameById("abc");
        jgr.joinGame(new Player("test2", null), gameId, password);

        assert (testGame.getPlayers().size() == 2);
    }


    @Test(expected = WordSweeperException.class)
    public void error_UserAlreadyInGame() throws WordSweeperException {
        JoinGameRequestController jgr = new JoinGameRequestController();
        CreateGameRequestController cgr = new CreateGameRequestController();
        Player owner = new Player("test", null);
        String gameId = "abc";
        String password = "abc";
        try {
            cgr.createGame(owner, gameId, password);
        } catch (WordSweeperException ex) {
            fail();
        }

        jgr.joinGame(owner, gameId, null);
    }
}
