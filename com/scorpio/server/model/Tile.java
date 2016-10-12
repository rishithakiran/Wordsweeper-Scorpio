package com.scorpio.server.model;

import java.util.Hashtable;
import java.util.Random;

import com.scorpio.server.accessory.Coordinate;

public class Tile implements IModel {

	final String characters = "ABCDEFGHIJKLMONPQRSTUVWXYZ";
	private static final Hashtable<Character, Integer> scoreforChar = new Hashtable<Character, Integer>() {
		{
			put('A', 1);
			put('B', 256);
			put('C', 3);
			put('D', 27);
			put('E', 32);
			put('F', 65536);
			put('G', 1);
			put('H', 256);
			put('I', 3);
			put('J', 27);
			put('K', 32);
			put('L', 65536);
			put('M', 1);
			put('N', 256);
			put('O', 3);
			put('P', 27);
			put('Q', 32);
			put('R', 65536);
			put('S', 65536);
			put('T', 1);
			put('U', 256);
			put('V', 3);
			put('W', 27);
			put('X', 32);
			put('Y', 65536);
			put('Z', 65536);

		}
	};
	final int N = characters.length();

	private Coordinate location;

	private char contents;
	private int points;
	private int multiplier;

	private int sharedBy;

	public Tile() {
		Random random = new Random();
		this.contents = characters.charAt(random.nextInt(N));
		this.points = scoreforChar.get(this.contents);

	}

	public Coordinate getLocation() {
		return location;
	}

	public void setLocation(Coordinate location) {
		this.location = location;
	}

	public char getContents() {
		return contents;
	}

	public void setContents(char contents) {
		this.contents = contents;
	}

	public int getPoits() {
		return points;
	}

	public void setPoits(int poits) {
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

	public void setSharedBy(int sharedBy) {
		this.sharedBy = sharedBy;
	}

}
