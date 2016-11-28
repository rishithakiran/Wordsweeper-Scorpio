package com.scorpio.test.unit.server.controller;

import com.scorpio.server.controller.GameAccessController;
import com.scorpio.server.controller.GameActionController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.*;
import com.scorpio.test.util.FakeClientState;
import com.scorpio.test.util.XMLUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class GameActionControllerTest {
	private XMLUtil xml = new XMLUtil();

	@Before
	public void resetGameManager() {
		GameManager.reset();
	}


	@Test(expected = WordSweeperException.class)
	public void error_WordDoesNotExist() throws WordSweeperException {
		GameAccessController gacc = new GameAccessController();
		GameActionController gact = new GameActionController();
		Player aPlayer = new Player("aPlayer", new FakeClientState("a"));
		try {
			gacc.createGame(aPlayer, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}

		gact.findWord(new Word(new ArrayList<>()), "aPlayer", "mygame");
	}


	@Test(expected = WordSweeperException.class)
	public void error_PlayerDoesNotExist() throws WordSweeperException {
		GameAccessController gacc = new GameAccessController();
		GameActionController gact = new GameActionController();
		Player aPlayer = new Player("aPlayer", new FakeClientState("a"));

		try {
			gacc.createGame(aPlayer, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}

		gact.findWord(new Word(new ArrayList<>()), "bPlayer", "mygame");
	}


	@Test(expected = WordSweeperException.class)
	public void error_GameDoesNotExist() throws WordSweeperException {
		GameAccessController gacc = new GameAccessController();
		GameActionController gact = new GameActionController();
		Player aPlayer = new Player("aPlayer", new FakeClientState("a"));

		try {
			gacc.createGame(aPlayer, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}

		gact.findWord(new Word(new ArrayList<>()), "aPlayer", "notmygame");
	}
}
