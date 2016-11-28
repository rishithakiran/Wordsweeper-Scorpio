package com.scorpio.test.unit.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Tile;
import com.scorpio.server.model.Word;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class WordTest {

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

        assertEquals(0,w.computeScore());
        assertEquals(3520,w2.computeScore());
    }

    @Test
    public void testComputeScore2() {
        Game g = new Game();
        Coordinate location= new Coordinate(1, 1) ;
        Coordinate location1= new Coordinate(1, 2) ;
        Coordinate location2= new Coordinate(1, 3) ;
        Coordinate location3= new Coordinate(1, 4) ;
        Coordinate location4= new Coordinate(2, 4) ;
        Tile t= new Tile("R",location);
        Tile t1= new Tile("E",location1);
        Tile t2= new Tile("C",location2);
        Tile t3= new Tile("O", location3);
        Tile t4= new Tile("R", location4);
        Tile t5= new Tile("D", location4);

        ArrayList<Tile> al = new ArrayList<Tile>();
        al.add(t);
        al.add(t1);
        al.add(t2);
        al.add(t3);
        al.add(t4);
        al.add(t5);

        Word w2=new Word(al);

        assertEquals(8320,w2.computeScore());
    }
}
