package com.scorpio.test.unit.server.controller;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.controller.CreateGameRequestController;
import com.scorpio.server.controller.FindWordRequestController;
import com.scorpio.server.controller.JoinGameRequestController;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.*;
import com.scorpio.test.util.FakeClientState;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.fail;

public class FindWordRequestControllerTest {
	@Before
	public void resetGameManager() {
		GameManager.reset();
	}

	@Test
	public void functionality_FindWordBasic(){
		CreateGameRequestController cgr = new CreateGameRequestController();
		FindWordRequestController fwr = new FindWordRequestController();

		Player aPlayer = new Player("aPlayer", new FakeClientState("a"));
		try {
			cgr.createGame(aPlayer, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}
		aPlayer.setLocation(new Coordinate(1,1));


		Board b = new RandomBoard(7);
		String boardString =("R B C D E F G" +
							 "E I J K L M N" +
							 "C P Q R S T U" +
							 "O R D Y Z A B" +
							 "C D E F G H I" +
							 "J K L M N O P" +
							 "Q R S T U V W")
				.replaceAll("\\s+","");
		for(int row = 0; row < b.getSize(); row++){
			for(int col = 0; col < b.getSize(); col++){
				b.getTileAt(new Coordinate(col+1,row+1)).setContents(String.valueOf(boardString.charAt((7 * row) + col)));
			}
		}

		GameManager.getInstance().findGameById("mygame").setBoard(b);

		assert(aPlayer.getScore() == 0);

		ArrayList<Tile> tiles = new ArrayList<>();
		tiles.add(new Tile("R", new Coordinate(1,1)));
		tiles.add(new Tile("E", new Coordinate(1,2)));
		tiles.add(new Tile("C", new Coordinate(1,3)));
		tiles.add(new Tile("O", new Coordinate(1,4)));
		tiles.add(new Tile("R", new Coordinate(2,4)));
		tiles.add(new Tile("D", new Coordinate(3,4)));
		Word w = new Word(tiles);

		try {
			fwr.findWord(w, aPlayer.getName(), "mygame");
		}catch(WordSweeperException e){
			fail();
		}


		int score = aPlayer.getScore();
		assert(score == 8320);

		// Ensure that the board has been updated
		// We don't need to check all tiles, BoardTest does this more completely
		assert(b.getTileAt(new Coordinate(1, 1)).getContents().equals("C"));
		assert(b.getTileAt(new Coordinate(1, 2)).getContents().equals("J"));
		assert(b.getTileAt(new Coordinate(1, 3)).getContents().equals("Q"));
	}

	@Test(expected = WordSweeperException.class)
	public void error_WordDoesNotExist() throws WordSweeperException {
		CreateGameRequestController cgr = new CreateGameRequestController();
		FindWordRequestController fwr = new FindWordRequestController();
		Player aPlayer = new Player("aPlayer", new FakeClientState("a"));
		try {
			cgr.createGame(aPlayer, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}

		fwr.findWord(new Word(new ArrayList<>()), "aPlayer", "mygame");
	}


	@Test(expected = WordSweeperException.class)
	public void error_PlayerDoesNotExist() throws WordSweeperException {
		CreateGameRequestController cgr = new CreateGameRequestController();
		FindWordRequestController fwr = new FindWordRequestController();
		Player aPlayer = new Player("aPlayer", new FakeClientState("a"));

		try {
			cgr.createGame(aPlayer, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}

		fwr.findWord(new Word(new ArrayList<>()), "bPlayer", "mygame");
	}


	@Test(expected = WordSweeperException.class)
	public void error_GameDoesNotExist() throws WordSweeperException {
		CreateGameRequestController cgr = new CreateGameRequestController();
		FindWordRequestController fwr = new FindWordRequestController();
		Player aPlayer = new Player("aPlayer", new FakeClientState("a"));

		try {
			cgr.createGame(aPlayer, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}

		fwr.findWord(new Word(new ArrayList<>()), "aPlayer", "notmygame");
	}

	@Test
	public void functionality_SharedBoardMultiplier2(){
		CreateGameRequestController cgr = new CreateGameRequestController();
		JoinGameRequestController jgr = new JoinGameRequestController();
		FindWordRequestController fwr = new FindWordRequestController();

		Player aPlayer = new Player("aPlayer", new FakeClientState("a"));
		Player bPlayer = new Player("bPlayer", new FakeClientState("b"));
		try {
			cgr.createGame(aPlayer, "mygame", null);
			jgr.joinGame(bPlayer, "mygame", null);
		}catch(WordSweeperException ex){
			fail();
		}

		aPlayer.setLocation(new Coordinate(1,1));
		bPlayer.setLocation(new Coordinate(1,4));
		// Player B overlaps starting on row 4, so the tiles
		// R, E, and C should only be shared by 1 player, while
		// O, R, and D are shared by 2

		Board b = new RandomBoard(7);
		String boardString =("R B C D E F G" +
							 "E I J K L M N" +
							 "C P Q R S T U" +
							 "O R D Y Z A B" +
							 "C D E F G H I" +
							 "J K L M N O P" +
							 "Q R S T U V W")
				.replaceAll("\\s+","");
		for(int row = 0; row < b.getSize(); row++){
			for(int col = 0; col < b.getSize(); col++){
				b.getTileAt(new Coordinate(col+1,row+1)).setContents(String.valueOf(boardString.charAt((7 * row) + col)));
			}
		}

		GameManager.getInstance().findGameById("mygame").setBoard(b);

		assert(aPlayer.getScore() == 0);

		ArrayList<Tile> tiles = new ArrayList<>();
		tiles.add(new Tile("R", new Coordinate(1,1)));
		tiles.add(new Tile("E", new Coordinate(1,2)));
		tiles.add(new Tile("C", new Coordinate(1,3)));
		tiles.add(new Tile("O", new Coordinate(1,4)));
		tiles.add(new Tile("R", new Coordinate(2,4)));
		tiles.add(new Tile("D", new Coordinate(3,4)));
		Word w = new Word(tiles);

		try {
			fwr.findWord(w, aPlayer.getName(), "mygame");
		}catch(WordSweeperException e){
			fail();
		}


		int score = aPlayer.getScore();
		assert(score == 21760);
	}
}
