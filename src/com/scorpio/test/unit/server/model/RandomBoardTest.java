package com.scorpio.test.unit.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.RandomBoard;
import com.scorpio.server.model.Tile;
import com.scorpio.server.model.Word;
import org.junit.Test;

import java.util.ArrayList;
/**
 * Test cases for all functionalities related to Random Board class.
 * @author Josh
 */

public class RandomBoardTest {
	/**Ensure the coordinates axis of the created random board is valid*/
   @Test
   public void functionality_CoordsCorrect(){
       Board b = new RandomBoard(7);

       String[] chars = b.toString().split(",");

       // Ensure that the coordinate axis is correct
       for(int row = 0; row < 7; ++row){
           for(int col = 0; col < 7; ++col){
                String c = chars[(row * 7) + col];
                String tc = b.getTileAt(new Coordinate(col + 1, row + 1)).getContents();
                assert(tc.equals(c));
           }
       }
   }
}
