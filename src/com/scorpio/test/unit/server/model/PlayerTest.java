package com.scorpio.test.unit.server.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.model.Player;

public class PlayerTest {
	@Test
	public void testSetPlayerName() {
	    String name="abc";
	    Player p = new Player(name, null);
	    p.setName(name);
	    assertEquals(p.getName(), name);
	}
	
	@Test
	public void testGetPlayerName() {
		Player p = new Player("abc" ,null);
		String name="abc";
		p.setName(name);
	    String result = p.getName();;
	    assertEquals(name, result);
	}
	
	@Test
	public void testgetClientState() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testSetLocation(){
		Coordinate location= new Coordinate(0, 1) ;
		Player p = new Player("abc", null);
		p.setLocation(location);
		assert(location==p.getLocation());	
	}
	
	@Test
	public void testGetLocation(){
		Coordinate location=new Coordinate(0, 1);
		Player p = new Player("abc", null);
		p.setLocation(location);
		Coordinate result=p.getLocation();
		assert(location==result);
	}
	
	@Test
	public void testSetScore(){
		int score=100;
		Player p = new Player("abc", null);
		p.setScore(score);
		assert(score==p.getScore());
}
	
	@Test
	public void testGetScore(){
		int score=100;
		Player p = new Player("abc", null);
		p.setScore(score);
		int result=p.getScore();
		assert(score==result);
	}
	
	@Test
	public void testSetisManagingUser(){
		boolean isManagingUser=true;
		Player p = new Player("abc", null);
		p.setManagingUser(isManagingUser);
		assert(isManagingUser==p.isManagingUser());		
	}
	
	@Test
	public void testGetisManagingUser(){
		boolean isManagingUser=true;
		Player p = new Player("abc", null);
		p.setManagingUser(isManagingUser);
		boolean result=p.isManagingUser();
		assert(isManagingUser==result);		
	}


	

}
