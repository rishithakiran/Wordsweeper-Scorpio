package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
/**
 * Test cases for Creating game request.
 * @author Josh
 *
 */
public class CreateGameRequestControllerTest {
    @Before
    public void resetGameManager() {
        GameManager.reset();
    }

    @Test
    /**
     * Ensure a game is created and verify the number of players, board size and managing users.
     * @throws WordSweeperException
     */
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

    /**
     * Ensure already created game is not recreated.
     * @throws WordSweeperException
     */
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
