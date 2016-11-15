package com.scorpio.server.model;

import java.util.Hashtable;
import java.util.Random;

import com.scorpio.server.accessory.Coordinate;

public class Tile implements IModel {

	final String characters = "abcdefghijklmnopqrstuvwxyz";

	public boolean markedForDelete = false;

	/**
	 * Stores the score for each character in the form of Hash Table
	 */
	private static final Hashtable<String, Integer> scoreforChar = new Hashtable<String, Integer>() {
		{
			put("a", 2);
			put("b", 4);
			put("c", 3);
			put("d", 3);
			put("e", 1);
			put("f", 4);
			put("g", 4);
			put("h", 2);
			put("i", 2);
			put("j", 7);
			put("k", 5);
			put("l", 3);
			put("m", 3);
			put("n", 2);
			put("o", 2);
			put("p", 4);
			put("q", 8);
			put("r", 2);
			put("s", 2);
			put("t", 1);
			put("u", 3);
			put("v", 5);
			put("w", 3);
			put("x", 7);
			put("y", 4);
			put("z", 8);
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
	public Tile(String value, Coordinate location){
		this.contents = value;
		this.points = scoreforChar.get(this.contents);
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
		/*
		if(otherTile.getMultiplier() != this.getMultiplier()){
			return false;
		}
		if(otherTile.getPoints() != this.getPoints()){
			return false;
		}
		if(otherTile.getSharedBy() != this.getSharedBy()){
			return false;
		}
		*/
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

	public String toString(){
		return this.contents;
	}

}
