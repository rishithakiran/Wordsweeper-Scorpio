package com.scorpio.test.unit.server.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.model.Tile;

public class TileTest {

	@Test
	public void testSetLocation(){
		Coordinate location= new Coordinate(1, 1) ;
		Tile t= new Tile();
		t.setLocation(location);
		assert(location==t.getLocation());	
	}
	
	@Test
	public void testGetLocation(){
		Coordinate location=new Coordinate(1, 1);
		Tile t= new Tile();
		t.setLocation(location);
		Coordinate result=t.getLocation();
		assert(location==result);
	}
	public void testSetContents() {
		Tile t= new Tile();
	    t.setContents("A");
	    assertEquals("A",t.getContents());
	}
	public void testGetContents() {
		Tile t= new Tile();
	    t.setContents("A");
	    String result=t.getContents();
	    assertEquals("A",result);
	}

	public void testGetPoints() {
		Tile t= new Tile();
	    t.setContents("R");
	    int result=t.getPoints();
	    assertEquals(2,result);
	}
	public void testSetMultiplier() {
		Tile t= new Tile();
	    t.setBonus(true);
	    assert(t.isBonus());
	}

	public void testEquals() {
		Tile t= new Tile();
	    assert(true==t.equals(null));
	}

}
