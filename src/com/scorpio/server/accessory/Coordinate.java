package com.scorpio.server.accessory;
/**
 * Responsible for assigning the coordinate location for each tile.
 * The representation is (y,x) for each tile location in an XY plane. 
 * @author 	Josh
 */
public class Coordinate {
	/** Represents the row value.
	 *  The value of row starts from 1 and corresponds to the x coordinate in XY plane.
	 **/
	public int row; 
	/** Represents the column value.
	 *  The value of column starts from 1 and corresponds to the y coordinate in XY plane.
	 **/
    public int col; 
   /**
     * Constructor the inputs the row and column value.
     * @param	col		This represents the y coordinate of tile location (y,x).
     * @param 	row 	This represents the x coordinate of tile location (y,x).
     */
   public Coordinate(int col, int row){
        this.row = row;
        this.col = col;
    }

    /**
     * Serializes the location (col,row).
     */
    public String toString(){
        return String.format("%d,%d", col, row);
    }

   /**
     * Indicates whether some other object is "equal to" this one.
     */
   @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Coordinate))return false;
        Coordinate otherCoordinate = (Coordinate) other;
        if(otherCoordinate.row == this.row && otherCoordinate.col == this.col){
            return true;
        }
        return false;
    }
}

