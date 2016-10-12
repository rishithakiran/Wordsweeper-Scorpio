package com.scorpio.test.unit.server.model;
import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.RandomBoard;
import org.junit.Test;


public class BoardTest {

    @Test
    public void functionality_Random(){
        RandomBoard b = new RandomBoard(7);
        assert(b.getSize() == 7);

        String[] tiles = b.toString().split(",");
        assert(tiles.length == 49);

        RandomBoard b2 = new RandomBoard(7);
        assert(!(b.toString().equals(b2.toString())));
    }

    @Test
    public void functionality_SubBoard(){
        RandomBoard b = new RandomBoard(7);
        Board sub = b.getSubBoard(new Coordinate(0,0), 4);
        assert(sub.getSize() == 4);

        String[] tiles = sub.toString().split(",");
        assert(tiles.length == 16);


        // Probably don't need to test ALL tiles
        assert(b.getTileAt(new Coordinate(0,0)).equals(sub.getTileAt(new Coordinate(0,0))));
        assert(b.getTileAt(new Coordinate(1,1)).equals(sub.getTileAt(new Coordinate(1,1))));
        assert(b.getTileAt(new Coordinate(2,2)).equals(sub.getTileAt(new Coordinate(2,2))));
        assert(b.getTileAt(new Coordinate(3,3)).equals(sub.getTileAt(new Coordinate(3,3))));

    }
}
