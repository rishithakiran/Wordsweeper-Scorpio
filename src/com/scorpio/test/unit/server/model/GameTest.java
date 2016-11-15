package com.scorpio.test.unit.server.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

public class GameTest {

	@Test
	public void testSetId(){
		String id="123";
		Game g= new Game();
		g.setId(id);
		assertEquals(id, g.getId());
	}
	
	@Test
	public void testGetId(){
		String id="123";
		Game g= new Game();
		g.setId(id);
		String result=g.getId();
		assertEquals(id,result );
	}
	 
	@Test
	public void testSetIsLocked(){
		boolean isLocked=true;
		Game g= new Game();
		g.setLocked(isLocked);
		assert(isLocked==g.isLocked());
	}
	
	@Test
	public void testGetIsLocked(){
		boolean isLocked=true;
		Game g= new Game();
		g.setLocked(isLocked);
		boolean result=g.isLocked();
		assert(isLocked==result);
	}
	@Test
	public void testSetBoard(){
		Board subBoard = new Board(5);
		Game g= new Game();
		g.setBoard(subBoard);
		assert(subBoard==g.getBoard());
	}
	
	@Test
	public void testGetBoard(){
		Board subBoard = new Board(5);
		Board result; 
		Game g= new Game();
		g.setBoard(subBoard);
		result=g.getBoard();
		assert(subBoard==result);
	}
	
	@Test
	public void testSetPlayers(){
		Game g= new Game();
		ClientState state;
		Player p1 = new Player("abc" ,state);
		Player p2 = new Player("123" ,state);
		ArrayList<Player> players={p1,p2};
		g.setPlayers(players);
		assert(players==g.getPlayers());
	}
	@Test
	public void testGetPlayers(){
		Game g= new Game();
		ClientState state;
		Player p1 = new Player("abc" ,state);
		Player p2 = new Player("123" ,state);
		ArrayList<Player> players={p1,p2};
		g.setPlayers(players);
		ArrayList<Player> result=g.getPlayers();
		assert(players==result);
	}
	
	
	
	

}
