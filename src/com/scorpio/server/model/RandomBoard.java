package com.scorpio.server.model;


import com.scorpio.server.accessory.Coordinate;
/**
 * Returns a random board for the player in the game.
 * @author Josh
 *
 */
public class RandomBoard extends Board{
	/**
	 * Constructor that extends the Board class.
	 * @param size	The size of the random board.
	 */
    public RandomBoard(int size){
        super(size);
        this.fillRandom();
    }

    /**
     * When called, will fill all tiles on the board with a random
     * value.
     */
    private void fillRandom(){
        for (int row = 1; row <= this.size; row++) {
            for (int col = 1; col <= this.size; col++) {
                Coordinate coordinate = new Coordinate(col, row);
                Tile tile = new Tile();
                tile.setLocation(coordinate);
                this.tiles.add(tile);
            }
        }
    }
}
