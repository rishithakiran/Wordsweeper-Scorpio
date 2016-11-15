package com.scorpio.test.unit.server.model;
import java.util.ArrayList;

import org.junit.Test;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.model.Tile;
import com.scorpio.server.model.Word;


public class BoardTest {
    @Test
    public void funtionality_SetTileAt() {
        Board b = new RandomBoard(7);
        Tile testTile = new Tile("q", new Coordinate(2, 1));
        b.setTileAt(new Coordinate(2,1), testTile);
        assert(b.getTileAt(new Coordinate(2,1)).getContents().equals("q"));
    }

    @Test(expected = WordSweeperException.class)
    public void resiliency_InvalidWord() throws WordSweeperException{
        Board b = new RandomBoard(7);
        String boardString =("a b c d e f g" +
                             "h i j k l m n" +
                             "o p q r s t u" +
                             "v w x y z a b" +
                             "c d e f g h i" +
                             "j k l m n o p" +
                             "q r s t u v w")
                            .replaceAll("\\s+","");
        for(int x = 0; x < b.getSize(); x++){
            for(int y = 0; y < b.getSize(); y++){
                b.getTileAt(new Coordinate(x,y)).setContents(String.valueOf(boardString.charAt((7 * y) + x)));
            }
        }

        ArrayList<Tile> targets = new ArrayList<>();
        targets.add(new Tile("z", new Coordinate(0,0)));
        targets.add(new Tile("z", new Coordinate(0,1)));
        targets.add(new Tile("z", new Coordinate(0,2)));
        targets.add(new Tile("z", new Coordinate(0,3)));
        targets.add(new Tile("z", new Coordinate(0,4)));
        Word tWord = new Word(targets);

        b.removeWord(tWord);
    }

    @Test
    public void functionality_RemoveWord() throws WordSweeperException{
        Board b = new RandomBoard(7);
        String boardString =("a b c d e f g" +
                             "h i g k l m n" +
                             "o p a m e t u" +
                             "v w x y s a b" +
                             "c d e f g h i" +
                             "j k l m n o p" +
                             "q r s t u v w")
                .replaceAll("\\s+","");
        for(int x = 0; x < b.getSize(); x++){
            for(int y = 0; y < b.getSize(); y++){
                b.getTileAt(new Coordinate(x,y)).setContents(String.valueOf(boardString.charAt((7 * y) + x)));
            }
        }

        ArrayList<Tile> targets = new ArrayList<>();
        targets.add(new Tile("g", new Coordinate(2,1)));
        targets.add(new Tile("a", new Coordinate(2,2)));
        targets.add(new Tile("m", new Coordinate(3,2)));
        targets.add(new Tile("e", new Coordinate(4,2)));
        targets.add(new Tile("s", new Coordinate(4,3)));
        Word tWord = new Word(targets);

        b.removeWord(tWord);
        assert(b.getTileAt(new Coordinate(2, 1)).getContents().equals("x"));
        assert(b.getTileAt(new Coordinate(2, 2)).getContents().equals("e"));
        assert(b.getTileAt(new Coordinate(3, 2)).getContents().equals("y"));
        assert(b.getTileAt(new Coordinate(4, 2)).getContents().equals("g"));
        assert(b.getTileAt(new Coordinate(4, 3)).getContents().equals("n"));
    }



    @Test
    public void resiliency_SubBoardTooBig(){
        RandomBoard b = new RandomBoard(7);

        Board sb = b.getSubBoard(new Coordinate(0,0), 8);

        assert(sb == null);
    }

    @Test
    public void resiliency_SubBoardOOB(){
        RandomBoard b = new RandomBoard(7);

        Board sb = b.getSubBoard(new Coordinate(5,5), 4);

        assert(sb == null);
    }

    @Test
    public void functionality_SubBoardIdentity(){
        RandomBoard b = new RandomBoard(7);

        Board sb = b.getSubBoard(new Coordinate(0,0), 7);

        // This check could be better
        assert(sb != null);
    }

    @Test
    public void functionality_GetTile(){
        RandomBoard b = new RandomBoard(7);
        Tile t = b.getTileAt(new Coordinate(0,0));
        assert(t.getLocation().equals(new Coordinate(0,0)));
    }

    @Test
    public void resiliency_TileOOB(){
        RandomBoard b = new RandomBoard(7);
        Tile t = b.getTileAt(new Coordinate(8,8));
        assert(t == null);
    }

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
        Tile t1 = b.getTileAt(new Coordinate(0,0));
        Tile t2 = sub.getTileAt(new Coordinate(0,0));

        assert(b.getTileAt(new Coordinate(0,0)).equals(sub.getTileAt(new Coordinate(0,0))));
        assert(b.getTileAt(new Coordinate(1,1)).equals(sub.getTileAt(new Coordinate(1,1))));
        assert(b.getTileAt(new Coordinate(2,2)).equals(sub.getTileAt(new Coordinate(2,2))));
        assert(b.getTileAt(new Coordinate(3,3)).equals(sub.getTileAt(new Coordinate(3,3))));
    }
}
