package com.scorpio.test.unit.server.model;
import java.util.ArrayList;

import com.scorpio.server.core.GameManager;
import org.junit.Test;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.model.Tile;
import com.scorpio.server.model.Word;
/**
 * Test cases for all the functionalities related to Board class.
 * @author Saranya
 * @author Josh
 */
public class BoardTest {
	
	/**
	 * Test to verify that a character is associated with the Tile.
	 */
    @Test
    public void funtionality_SetTileAt() {
        Board b = new RandomBoard(7);
        Tile testTile = new Tile("Q", new Coordinate(2, 1));
        b.setTileAt(new Coordinate(2,1), testTile);
        assert(b.getTileAt(new Coordinate(2,1)).getContents().equals("Q"));
    }

    @Test(expected = WordSweeperException.class)
    public void resiliency_InvalidWord() throws WordSweeperException{
        Board b = new RandomBoard(7);
        String boardString =("A B C D E F G" +
                             "H I J K L M N" +
                             "O P Q R S T U" +
                             "V W X Y Z A B" +
                             "C D E F G H I" +
                             "J K L M N O P" +
                             "Q R S T U V W")
                            .replaceAll("\\s+","");
        for(int row = 0; row < b.getSize(); row++){
            for(int col = 0; col < b.getSize(); col++){
                b.getTileAt(new Coordinate(row+1,col+1)).setContents(String.valueOf(boardString.charAt((7 * col) + row)));
            }
        }

        ArrayList<Tile> targets = new ArrayList<>();
        targets.add(new Tile("Z", new Coordinate(1,1)));
        targets.add(new Tile("Z", new Coordinate(1,2)));
        targets.add(new Tile("Z", new Coordinate(1,3)));
        targets.add(new Tile("Z", new Coordinate(1,4)));
        targets.add(new Tile("Z", new Coordinate(1,5)));
        Word tWord = new Word(targets);

        b.removeWord(tWord);
    }

    /**
     * Ensure that the given input word is validated and the corresponding
     * tiles are removed and the board is updated with the tiles below.
     * @throws WordSweeperException
     */
    @Test
    public void functionality_RemoveWord() throws WordSweeperException{
        Board b = new RandomBoard(7);
        String boardString =("A B C D E F G" +
                             "H I G K L M N" +
                             "O P A M E T U" +
                             "V W X Y S A B" +
                             "C D E F G H I" +
                             "J K L M N O P" +
                             "Q R S T U V W")
                .replaceAll("\\s+","");
        for(int row = 0; row < b.getSize(); row++){
            for(int col = 0; col < b.getSize(); col++){
                b.getTileAt(new Coordinate(col + 1, row + 1)).setContents(String.valueOf(boardString.charAt((7 * row) + col)));
            }
        }

        ArrayList<Tile> targets = new ArrayList<>();
        targets.add(new Tile("G", new Coordinate(3,2)));
        targets.add(new Tile("A", new Coordinate(3,3)));
        targets.add(new Tile("M", new Coordinate(4,3)));
        targets.add(new Tile("E", new Coordinate(5,3)));
        targets.add(new Tile("S", new Coordinate(5,4)));
        Word tWord = new Word(targets);

        b.removeWord(tWord);
        assert(b.getTileAt(new Coordinate(3, 2)).getContents().equals("X"));
        assert(b.getTileAt(new Coordinate(3, 3)).getContents().equals("E"));
        assert(b.getTileAt(new Coordinate(4, 3)).getContents().equals("Y"));
        assert(b.getTileAt(new Coordinate(5, 3)).getContents().equals("G"));
        assert(b.getTileAt(new Coordinate(5, 4)).getContents().equals("N"));
    }

    /**
     * Ensure that a input word in a vertical line is validated
     * and corresponding tiles are removed from the board 
     * and the board is updated accordingly.
     * @throws WordSweeperException
     */
    @Test
    public void functionality_RemoveWordVertical() throws WordSweeperException{
        Board b = new RandomBoard(7);
        String boardString =("G B C D E F G" +
                             "O I J K L M N" +
                             "L P Q R S T U" +
                             "D W X Y Z A B" +
                             "C D E F G H I" +
                             "J K L M N O P" +
                             "Q R S T U V W")
                .replaceAll("\\s+","");
        for(int row = 0; row < b.getSize(); row++){
            for(int col = 0; col < b.getSize(); col++){
                b.getTileAt(new Coordinate(col+1,row+1)).setContents(String.valueOf(boardString.charAt((7 * row) + col)));
            }
        }

        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(new Tile("G", new Coordinate(1,1)));
        tiles.add(new Tile("O", new Coordinate(1,2)));
        tiles.add(new Tile("L", new Coordinate(1,3)));
        tiles.add(new Tile("D", new Coordinate(1,4)));
        Word w = new Word(tiles);

        b.removeWord(w);

        assert(b.getTileAt(new Coordinate(1, 1)).getContents().equals("C"));
        assert(b.getTileAt(new Coordinate(1, 2)).getContents().equals("J"));
        assert(b.getTileAt(new Coordinate(1, 3)).getContents().equals("Q"));
    }

    /**
     * Ensure that the sub-board size is within the bounds of the required
     * sub-board size.
     */
    @Test
    public void resiliency_SubBoardTooBig(){
        RandomBoard b = new RandomBoard(7);

        Board sb = b.getSubBoard(new Coordinate(1,1), 8);

        assert(sb == null);
    }
    
    /**
     * Ensure that the sub-board starting location does not result in out-of-bounds of 
     * that of the global board size. 
     */
    @Test
    public void resiliency_SubBoardOOB(){
        RandomBoard b = new RandomBoard(7);

        Board sb = b.getSubBoard(new Coordinate(5,5), 4);

        assert(sb == null);
    }

    @Test
    public void functionality_SubBoardIdentity(){
        RandomBoard b = new RandomBoard(7);
        Board sb = b.getSubBoard(new Coordinate(1,1), 7);

        // This check could be better
        assert(sb != null);
    }

    /**
     * Test to ensure the functionality of the tile in the board.
     */
    @Test
    public void functionality_GetTile(){
        RandomBoard b = new RandomBoard(7);
        Tile t = b.getTileAt(new Coordinate(1,1));
        assert(t.getLocation().equals(new Coordinate(1,1)));
    }
    
    /**
     * Test to ensure that the tile location is not out-of-bounds than that of board.
     */
    @Test
    public void resiliency_TileOOB(){
        RandomBoard b = new RandomBoard(7);
        Tile t = b.getTileAt(new Coordinate(8,8));
        assert(t == null);
    }
    
    /**
     * Test to ensure that a board is created of required size and verify the total
     * number of tiles in the board. And ensure that two randomly created board doesn't
     * have same contents.
     */
    @Test
    public void functionality_Random(){
        RandomBoard b = new RandomBoard(7);
        assert(b.getSize() == 7);

        String[] tiles = b.toString().split(",");
        assert(tiles.length == 49);

        RandomBoard b2 = new RandomBoard(7);
        assert(!(b.toString().equals(b2.toString())));
    }

    /**
     * Ensure that a sub-board of required size is created and verify the tile size. 
     * And also verify the location of the tile present in the global board 
     * is identified in the sub-board.
     */
    @Test
    public void functionality_SubBoard(){
        RandomBoard b = new RandomBoard(7);
        Board sub = b.getSubBoard(new Coordinate(1,1), 4);
        assert(sub.getSize() == 4);

        String[] tiles = sub.toString().split(",");
        assert(tiles.length == 16);

        // Probably don't need to test ALL tiles
        Tile t1 = b.getTileAt(new Coordinate(1,1));
        Tile t2 = sub.getTileAt(new Coordinate(1,1));

        assert(b.getTileAt(new Coordinate(1,1)).equals(sub.getTileAt(new Coordinate(1,1))));
        assert(b.getTileAt(new Coordinate(2,2)).equals(sub.getTileAt(new Coordinate(2,2))));
        assert(b.getTileAt(new Coordinate(3,3)).equals(sub.getTileAt(new Coordinate(3,3))));
        assert(b.getTileAt(new Coordinate(4,4)).equals(sub.getTileAt(new Coordinate(4,4))));

    }
}
