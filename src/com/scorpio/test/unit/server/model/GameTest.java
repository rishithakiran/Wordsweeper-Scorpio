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
	@Test
	public void testComputeScore() {
	    Game g = new Game();
	    Coordinate location= new Coordinate(1, 1) ;
	    Coordinate location1= new Coordinate(1, 2) ;
	    Coordinate location2= new Coordinate(1, 3) ;
	    Coordinate location3= new Coordinate(1, 4) ;
	    Coordinate location4= new Coordinate(2, 4) ;
	    Tile t= new Tile("C",location);
	    Tile t1= new Tile("R",location1);
	    Tile t2= new Tile("O",location2);
	    Tile t3= new Tile("S", location3);
	    Tile t4= new Tile("S", location4);
	    ArrayList<Tile> al = new ArrayList<Tile>();
	    al.add(t);
	    al.add(t1);
	    al.add(t2);
	    al.add(t3);
	    al.add(t4);
	    Word w2=new Word(al);
	    Word w=new Word(null);
	    assertEquals(0,g.computeScore("abc", w));
		assertEquals(1600,g.computeScore("abc", w2));
	}
	

}
