package com.scorpio.server.model;

import java.util.Hashtable;
import java.util.Random;

import com.scorpio.server.accessory.Coordinate;

public class Tile implements IModel {

	final String characters = "ABCDEFGHIJKLMONPQRSTUVWXYZ";
	private static final Hashtable<String, Integer> scoreforChar = new Hashtable<String, Integer>() {
		{
			put("A", 1);
			put("B", 256);
			put("C", 3);
			put("D", 27);
			put("E", 32);
			put("F", 65536);
			put("G", 1);
			put("H", 256);
			put("I", 3);
			put("J", 27);
			put("K", 32);
			put("L", 65536);
			put("M", 1);
			put("N", 256);
			put("O", 3);
			put("P", 27);
			put("Q", 32);
			put("R", 65536);
			put("S", 65536);
			put("T", 1);
			put("U", 256);
			put("V", 3);
			put("W", 27);
			put("X", 32);
			put("Y", 65536);
			put("Z", 65536);

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
