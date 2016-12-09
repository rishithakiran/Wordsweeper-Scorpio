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
/**
 * Test cases for Join game request.
 * @author Josh
 * @author Apoorva
 *
 */
public class JoinGameRequestControllerTest {
    @Before
    public void resetGameManager() {
        GameManager.reset();
    }

    /**
     * Ensure that only a created game can accept other players to join.
     * @throws WordSweeperException
     */
    @Test(expected = WordSweeperException.class)
    public void error_JoinGameDoesNotExist() throws WordSweeperException {
        JoinGameRequestController jgr = new JoinGameRequestController();
        Player owner = new Player("test", null);
        String gameId = "abc";
        String password = "abc";
        jgr.joinGame(owner, gameId, password);
    }

    /**
     * Ensure that other players can join the existing game.
     * And also verify the number of players in the current game.
     * @throws WordSweeperException
     */
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

    /**
     * Ensure that a player already a part of the current game 
     * cannot rejoin the active game.
     * @throws WordSweeperException
     */
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
