package com.scorpio.server.model;

import java.util.Hashtable;
import java.util.Random;

import com.scorpio.server.accessory.Coordinate;

public class Tile implements IModel {


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


	public Tile() {
		Random random = new Random();
		int randIndex = random.nextInt(scoreforChar.size());
		this.isBonus = false;
		this.sharedBy = 1;


		this.contents = (String)(scoreforChar.keySet().toArray())[randIndex];

	}
	public Tile(String value, Coordinate location){
		this.contents = value;
		this.isBonus = false;
		this.sharedBy = 1;
		this.location = location;
	}

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

	public void setSharedBy(int s){
		this.sharedBy = s;
	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getPoints() {
		return scoreforChar.get(this.contents);
	}

	public boolean isBonus() {
		return this.isBonus;
	}

	public void setBonus(boolean isBonus) {
		this.isBonus = isBonus;
	}

	public int getSharedBy() {
		return sharedBy;
	}

	public String toString(){
		return this.contents;
	}

}
