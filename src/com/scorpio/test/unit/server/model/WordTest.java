package com.scorpio.test.unit.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Tile;
import com.scorpio.server.model.Word;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
/**
 * Test cases for all functionalities related to the Word class.
 * @author Rishitha
 *
 */
public class WordTest {

	/**
	 * Test to verify the whether a valid input word is calculated correctly
	 *  with respect to the points associated with the contents and zero
	 *  for invalid word.
	 */
    @Test
    public void testComputeScore() throws WordSweeperException{
        Coordinate location = new Coordinate(1, 1) ;
        Coordinate location1 = new Coordinate(1, 2) ;
        Coordinate location2 = new Coordinate(1, 3) ;
        Coordinate location3 = new Coordinate(1, 4) ;
        Coordinate location4 = new Coordinate(2, 4) ;
        Tile t = new Tile("C",location);
        Tile t1 = new Tile("R",location1);
        Tile t2 = new Tile("O",location2);
        Tile t3 = new Tile("S", location3);
        Tile t4 = new Tile("S", location4);
        ArrayList<Tile> al = new ArrayList<Tile>();
        al.add(t);
        al.add(t1);
        al.add(t2);
        al.add(t3);
        al.add(t4);

        Word w2 = new Word(al);
        Word w = new Word(null);

        assertEquals(0,w.computeScore());
        assertEquals(3520,w2.computeScore());
    }

    /**
	 *  Test to verify the whether a valid input word is calculated correctly
	 *  with respect to the points associated with the contents.
	 */
    @Test
    public void testComputeScore2() throws WordSweeperException{
        Coordinate location = new Coordinate(1, 1) ;
        Coordinate location1 = new Coordinate(1, 2) ;
        Coordinate location2 = new Coordinate(1, 3) ;
        Coordinate location3 = new Coordinate(1, 4) ;
        Coordinate location4 = new Coordinate(2, 4) ;
        Coordinate location5 = new Coordinate(3, 4);
        Tile t= new Tile("R",location);
        Tile t1= new Tile("E",location1);
        Tile t2= new Tile("C",location2);
        Tile t3= new Tile("O", location3);
        Tile t4= new Tile("R", location4);
        Tile t5= new Tile("D", location5);

        ArrayList<Tile> al = new ArrayList<Tile>();
        al.add(t);
        al.add(t1);
        al.add(t2);
        al.add(t3);
        al.add(t4);
        al.add(t5);

        Word w2 = new Word(al);

        assertEquals(8320,w2.computeScore());
    }
}
