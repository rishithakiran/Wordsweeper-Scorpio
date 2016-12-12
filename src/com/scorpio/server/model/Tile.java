package com.scorpio.server.model;

import java.util.Hashtable;
import java.util.Random;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.accessory.RandomWeightedLetter;

/**
 * Includes functionalities associated with the Tiles of the board.
 * @author Apoorva
 * @author Saranya
 * @author Josh
 *
 */
public class Tile implements IModel {

	final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/**Value assigned for the Tiles, to know whether they are marked for deletion*/
	public boolean markedForDelete = false;

	/**
	 * Stores the score for each character in the form of Hash Table
	 */
	private static final Hashtable<String, Integer> scoreforChar = new Hashtable<String, Integer>() {
		{
			put("A", 2);
			put("B", 4);
			put("C", 3);
			put("D", 3);
			put("E", 1);
			put("F", 4);
			put("G", 4);
			put("H", 2);
			put("I", 2);
			put("J", 7);
			put("K", 5);
			put("L", 3);
			put("M", 3);
			put("N", 2);
			put("O", 2);
			put("P", 4);
            // We don't pick Q. Ever.
			//put("Q", 8);
			put("R", 2);
			put("S", 2);
			put("T", 1);
			put("U", 3);
			put("V", 5);
			put("W", 3);
			put("X", 7);
			put("Y", 4);
			put("Z", 8);
			put("Qu", 11);
		}
	};

	private Coordinate location;
	private String contents;
	private boolean isBonus;
	private int sharedBy;

    /**
     * Construct the tile with a random value
     */
	public Tile() {
		this.isBonus = false;
		this.sharedBy = 0;
		this.contents = new RandomWeightedLetter().getValue();
	}

	/**
     * Construct the tile with a specific value and location. This is largely
     * used in creating subboards.
     * @param value Character value to be stored in this tile
     * @param location Coordinate location at which the tile will be
     */
	public Tile(String value, Coordinate location){
		this.contents = value;
		this.isBonus = false;
		this.sharedBy = 0;
		this.location = location;
	}

	/**Indicates whether some other object is "equal to" this one.*/
	@Override
	public boolean equals(Object other){
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Tile))return false;
		Tile otherTile = (Tile) other;
		if(!otherTile.getLocation().equals(this.getLocation())){
			return false;
		}
		if(!otherTile.getContents().equals(this.getContents())){
			return false;
		}
		return true;
	}

	/**
     * Setter for sharedBy value. You probably don't need this function.
     * It's used to store numbers for temporary tiles, so please don't
     * use it on tiles of the main board of a game. Just leave them as 0.
     * @param s The value to set
     */
	public void setSharedBy(int s){
		this.sharedBy = s;
	}

    /**
     * Getter for location
     * @return location
     */
	public Coordinate getLocation() {
		return location;
	}

    /**
     * Setter for location
     * @param location
     */
	public void setLocation(Coordinate location) {
		this.location = location;
	}

	/**
     * Getter for string contents of this tile
     * @return the value of this tile
     */
	public String getContents() {
		return contents;
	}

    /**
     * Setter for tile contents
     * @param contents
     */
	public void setContents(String contents) {
		this.contents = contents;
	}

    /**
     * Getter for the score of this tile based on its string contents
     * @return Raw point value of this tile
     */
	public int getPoints() {
		return scoreforChar.get(this.contents);
	}

    /**
     * Getter for whether or not this tile is a bonus tile
     * @return Is this tile a bonus?
     */
	public boolean isBonus() {
		return this.isBonus;
	}

	/**
     * Setter for bonus status of this tile
     * @param isBonus
     */
	public void setBonus(boolean isBonus) {
		this.isBonus = isBonus;
	}

    /**
     * Getter for sharedBy value. You really don't need this
     * @return How many players is this tile shared by?
     */
	public int getSharedBy() {
		return sharedBy;
	}

    /**
     * Serializes this tile
     * @return See getContents()
     */
	public String toString(){
		return this.contents;
	}
}
