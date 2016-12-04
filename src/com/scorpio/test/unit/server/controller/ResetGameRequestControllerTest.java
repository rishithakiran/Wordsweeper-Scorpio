package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.ResetGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.test.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

public class ResetGameRequestControllerTest {
	private XMLUtil xml = new XMLUtil();

	@Before
	public void resetGameManager() {
		GameManager.reset();
	}

	@Test
	public void functionality_Basic() throws WordSweeperException {
        CreateGameRequestController cgr = new CreateGameRequestController();
        Player owner = new Player("test", null);
        owner.setScore(15);
        String gameId = "abc";
        cgr.createGame(owner, gameId, null);

        Game testGame = GameManager.getInstance().findGameById("abc");
        String oldBoard = testGame.getBoard().toString();

        ResetGameRequestController rgr = new ResetGameRequestController();
        rgr.resetGame("abc");

        assert(!testGame.getBoard().toString().equals(oldBoard));
        assert(owner.getScore() == 0);
	}
}