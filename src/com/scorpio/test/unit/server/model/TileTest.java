package com.scorpio.test.unit.server.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.Tile;
/**
 * Test cases for all functionalities related to the Tile class.
 * @author Rishitha
 */
public class TileTest {

	/**Test to verify the functionalities for mapping tile with corresponding coordinates*/
	@Test
	public void testSetLocation(){
		Coordinate location= new Coordinate(1, 1) ;
		Tile t = new Tile();
		t.setLocation(location);
		assert(location == t.getLocation());	
	}
	/**Test to verify the functionalities for obtaining coordinate location of a tile*/
	@Test
	public void testGetLocation(){
		Coordinate location=new Coordinate(1, 1);
		Tile t = new Tile();
		t.setLocation(location);
		Coordinate result = t.getLocation();
		assert(location == result);
	}
	/**Test to verify the functionalities for assigning the contents to a tile*/
	@Test
	public void testSetContents() {
		Tile t = new Tile();
	    t.setContents("A");
	    assertEquals("A",t.getContents());
	}
	/**Test to verify the functionalities for retrieving the contents from a tile*/
	@Test
	public void testGetContents() {
		Tile t = new Tile();
	    t.setContents("A");
	    String result = t.getContents();
	    assertEquals("A",result);
	}
	/**Test to verify the functionalities for assigning points to the contents in a tile*/
	@Test
	public void testGetPoints() {
		Tile t = new Tile();
	    t.setContents("R");
	    int result = t.getPoints();
	    assertEquals(2,result);
	}
	/**Test to verify the functionalities for assigning multiplier to a tile*/
	@Test
	public void testSetMultiplier() {
		Tile t = new Tile();
	    t.setBonus(true);
	    assert(t.isBonus());
	}
	
	@Test
	public void testEquals() {
		Tile t = new Tile();
	    assert(true == t.equals(null));
	}
}
