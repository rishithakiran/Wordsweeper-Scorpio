package com.scorpio.test.unit.server.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Tile;
import com.scorpio.server.model.Word;

public class GameTest {

	@Test
	public void testSetId() {
	    Game g = new Game();
	    g.setId("123");
	    assertEquals("123",g.getId());
	}
	@Test
	public void testGetId() {
	    Game g = new Game();
	    g.setId("123");
	    String result=g.getId();
	    assertEquals("123",result);
	}
	@Test
	public void testSetIsLocked() {
	    Game g = new Game();
	    g.setLocked(true);
	    assert(true==g.isLocked());
	}
	@Test
	public void testIsLocked() {
	    Game g = new Game();
	    g.setLocked(true);
	    Boolean result=g.isLocked();
	    assert(true==result);
	}
	@Test
	public void testSetBoard() {
	    Game g = new Game();
	    Board board = new Board(7);
	    g.setBoard(board);
	    assert(board==g.getBoard());
	}
	@Test
	public void testGetBoard() {
	    Game g = new Game();
	    Board board = new Board(7);
	    Board result = new Board(7);
	    g.setBoard(board);
	    result=g.getBoard();
	    assert(board==result);
	}
	@Test
	public void testSetPassword() {
	    Game g = new Game();
	    g.setPassword("password");
	    assertEquals("password",g.getPassword());
	}
	@Test
	public void testGetPassword() {
	    Game g = new Game();
	    g.setPassword("password");
	    String result=g.getPassword();
	    assertEquals("password",result);
	}

}
