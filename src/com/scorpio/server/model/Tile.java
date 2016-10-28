package com.scorpio.server.model;

import java.util.Hashtable;
import java.util.Random;

import com.scorpio.server.accessory.Coordinate;

public class Tile implements IModel {

	final String characters = "ABCDEFGHIJKLMONPQRSTUVWXYZ";
	
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
			put("Q", 8);
			put("R", 2);
			put("S", 2);
			put("T", 1);
			put("U", 3);
			put("V", 5);
			put("W", 3);
			put("X", 7);
			put("Y", 4);
			put("Z", 8);
		}
	};
	final int N = characters.length();

	private Coordinate location;

	private String contents;
	private int points;
	private int multiplier;

	private int sharedBy;


	public Tile() {
		Random random = new Random();
		int randIndex = random.nextInt(N);
		this.contents = characters.substring(randIndex, randIndex+1);
		this.points = scoreforChar.get(this.contents);

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
		if(otherTile.getMultiplier() != this.getMultiplier()){
			return false;
		}
		if(otherTile.getPoints() != this.getPoints()){
			return false;
		}
		if(otherTile.getSharedBy() != this.getSharedBy()){
			return false;
		}
		return true;
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
		return points;
	}

	public void setPoints(int poits) {
		this.points = poits;
	}

	public int getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}

	public int getSharedBy() {
		return sharedBy;
	}

}
