package com.scorpio.test.unit.server.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Tile;
import com.scorpio.server.model.Word;
/**
 * Test cases for all the functionalities related to Game class.
 * @author Rishitha
 *
 */
public class GameTest {
	/**Ensure that the game holds only valid players*/
	@Test
	public void testGetNonExistentPlayer(){
		Game g = new Game();
		assert(g.getPlayer("I'm not real") == null);
	}
	
	/**Ensure that assigned game ID is correct*/
	@Test
	public void testSetId() {
	    Game g = new Game();
	    g.setId("123");
	    assertEquals("123",g.getId());
	}
	/**Test to verify the game ID*/
	@Test
	public void testGetId() {
	    Game g = new Game();
	    g.setId("123");
	    String result = g.getId();
	    assertEquals("123",result);
	}
	/**Test to verify lock functionality*/
	@Test
	public void testSetIsLocked() {
	    Game g = new Game();
	    g.setLocked(true);
	    assert(true == g.isLocked());
	}
	/**Ensure whether a game is locked or not*/
	@Test
	public void testIsLocked() {
	    Game g = new Game();
	    g.setLocked(true);
	    Boolean result = g.isLocked();
	    assert(true == result);
	}
	/**Test to ensure board is created in a game*/
	@Test
	public void testSetBoard() {
	    Game g = new Game();
	    Board board = new Board(7);
	    g.setBoard(board);
	    assert(board == g.getBoard());
	}
	/**Test to verify the functionality of get board from game*/
	@Test
	public void testGetBoard() {
	    Game g = new Game();
	    Board board = new Board(7);
	    Board result = new Board(7);
	    g.setBoard(board);
	    result = g.getBoard();
	    assert(board == result);
	}
	/**Test to verify the functionality of cresting a password for a game*/
	@Test
	public void testSetPassword() {
	    Game g = new Game();
	    g.setPassword("password");
	    assertEquals("password",g.getPassword());
	}
	/**Test to verify the functionality of getting the created password*/ 
	@Test
	public void testGetPassword() {
	    Game g = new Game();
	    g.setPassword("password");
	    String result = g.getPassword();
	    assertEquals("password",result);
	}

}
