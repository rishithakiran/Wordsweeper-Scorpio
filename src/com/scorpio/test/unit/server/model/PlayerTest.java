package com.scorpio.test.unit.server.model;

import static org.junit.Assert.*;

import com.scorpio.test.util.FakeClientState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.model.Player;
/**
 * Test cases for all the functionalities related to Player class.
 * @author Rishitha
 *
 */
public class PlayerTest {
	/**Test to verify the functionalities for assigning a player name*/
	@Test
	public void testSetPlayerName() {
	    Player p = new Player("abc", null);
	    p.setName("abc");
	    assertEquals(p.getName(), "abc");
	}
	/**Test to verify the functionalities for getting the player name*/
	@Test
	public void testGetPlayerName() {
		Player p = new Player("abc" ,null);
		p.setName("abc");
	    String result = p.getName();;
	    assertEquals("abc", result);
	}
	/**Test to verify the functionalities for getting the Client state*/
	@Test
	public void testgetClientState() {
		FakeClientState fcs = new FakeClientState("p");
		Player p = new Player("abc" ,fcs);
		assert(fcs == p.getClientState());
	}
	/**Test to verify the functionalities for assigning a location for the player*/
	@Test
	public void testSetLocation(){
		Coordinate location= new Coordinate(1, 1) ;
		Player p = new Player("abc", null);
		p.setLocation(location);
		assert(location == p.getLocation());	
	}
	/**Test to verify the functionalities for retrieving the location of the player*/
	@Test
	public void testGetLocation(){
		Coordinate location=new Coordinate(1, 1);
		Player p = new Player("abc", null);
		p.setLocation(location);
		Coordinate result = p.getLocation();
		assert(location == result);
	}
	/**Test to verify the functionalities for assigning the score for a player*/
	@Test
	public void testSetScore(){
		Player p = new Player("abc", null);
		p.setScore(100);
		assert(100 == p.getScore());
	}
	/**Test to verify the functionalities for obtaining the score of the player*/
	@Test
	public void testGetScore(){
		Player p = new Player("abc", null);
		p.setScore(100);
		int result = p.getScore();
		assert(100 == result);
	}
	/**Test to verify the functionalities for assigning a player as managing user*/
	@Test
	public void testSetisManagingUser(){
		Player p = new Player("abc", null);
		p.setManagingUser(true);
		assert(true == p.isManagingUser());		
	}
	/**Test to verify the functionalities for retrieving the managing user of the game*/
	@Test
	public void testGetisManagingUser(){
		Player p = new Player("abc", null);
		p.setManagingUser(true);
		boolean result = p.isManagingUser();
		assert(true == result);		
	}
}
