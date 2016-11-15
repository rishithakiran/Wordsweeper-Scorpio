package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.GameAccessController;
import com.scorpio.server.controller.GameManagementController;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.test.util.TestHandler;
import com.scorpio.test.util.Trigger;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class GameManagementControllerTest {
	private XMLUtil xml = new XMLUtil();

	@Before
	public void resetGameManager() {
		GameManager.reset();
	}

	@Test
	public void functionality_Basic() throws WordSweeperException {
        GameAccessController gac = new GameAccessController();
        Player owner = new Player("test", null);
        owner.setScore(15);
        String gameId = "abc";
        gac.createGame(owner, gameId, null);

        Game testGame = GameManager.getInstance().findGameById("abc");
        String oldBoard = testGame.getBoard().toString();

        GameManagementController gmc = new GameManagementController();
        gmc.resetGame("abc");

        assert(!testGame.getBoard().toString().equals(oldBoard));
        assert(owner.getScore() == 0);

	}
}