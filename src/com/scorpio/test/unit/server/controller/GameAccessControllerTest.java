package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.controller.GameAccessController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.test.util.TestHandler;
import com.scorpio.test.util.Trigger;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

import java.util.HashMap;

public class GameAccessControllerTest {
	private XMLUtil xml = new XMLUtil();

	@Before
	public void resetGameManager() {
		GameManager.reset();
	}

	@Test
	public void functionality_CreateGame() throws WordSweeperException {

		GameAccessController gac = new GameAccessController();
		Player owner = new Player("test", null);
		String gameId = "abc";
		String password = "abc";
		gac.createGame(owner, gameId, password);

		Game testGame = GameManager.getInstance().findGameById("abc");
		assert (testGame != null);
		assert (testGame.getManagingPlayer() == owner);
		assert (testGame.getPlayers().size() == 1);
		assert (testGame.getBoard().getSize() == 7);

	}

	@Test
	public void functionality_JoinGame() throws WordSweeperException {
		GameAccessController gac = new GameAccessController();
		Player owner = new Player("test", null);
		String gameId = "abc";
		String password = "abc";
		gac.createGame(owner, gameId, null);

		Game testGame = GameManager.getInstance().findGameById("abc");
		gac.joinGame(new Player("test2", null), gameId, password);

		assert (testGame.getPlayers().size() == 2);
	}

	@Test(expected = WordSweeperException.class)
	public void error_UserAlreadyInGame() throws WordSweeperException {
		GameAccessController gac = new GameAccessController();
		Player owner = new Player("test", null);
		String gameId = "abc";
		String password = "abc";
		try {
			gac.createGame(owner, gameId, password);
		} catch (WordSweeperException ex) {
		}
		// We aren't checking for error here in this test, but we // won't be
		// able to run this test if this is failing, // so fast fail fail(); }
		gac.joinGame(owner, gameId, null);
	}

	@Test(expected = WordSweeperException.class)
	public void error_GameAlreadyExists() throws WordSweeperException {
		GameAccessController gac = new GameAccessController();
		Player owner = new Player("test", null);
		String gameId = "abc";
		String password = "abc";
		try {
			gac.createGame(owner, gameId, password);
		} catch (WordSweeperException ex) {
		}
		// We aren't checking for error here in this test, but we // won't be
		// able to run this test if this is failing, // so fast fail // If this
		// is failing, createGame isn't working period fail(); }

		gac.createGame(new Player("test2", null), gameId, null);
	}

	@Test(expected = WordSweeperException.class)
	public void error_GameDoesNotExist() throws WordSweeperException {
		GameAccessController gac = new GameAccessController();
		Player owner = new Player("test", null);
		String gameId = "abc";
		String password = "abc";
		gac.joinGame(owner, gameId, password);
	}
}
