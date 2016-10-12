package com.scorpio.server.model;

import java.util.ArrayList;
import java.util.Random;

import com.scorpio.server.accessory.Coordinate;

public class Board implements IModel {

	private ArrayList<Tile> tiles;
	private int size;

	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(ArrayList<Tile> tiles) {
		this.tiles = tiles;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Board() {
		ArrayList<Tile> tiles = new ArrayList<>();
		this.size = 4;
		for (int row = 1; row <= 4; row++) {
			for (int column = 1; column <= 4; column++) {
				Coordinate coordinate = new Coordinate();
				coordinate.x = row;
				coordinate.y = column;
				Tile tile = new Tile();
				tile.setLocation(coordinate);
				tiles.add(tile);
			}
		}
		this.tiles = tiles;
	}

	public boolean isValidWord(Word w) {
		return false;
	}

	public Board grow(Board board, int size) {

		ArrayList<Tile> tiles = new ArrayList<>();
		Random random = new Random();
		int max = board.getSize() + size;
		Board newBoard = new Board();
		for (Tile tile : board.getTiles()) {
			tiles.add(tile);
		}
		// the first board in the game will be created at position 0
		int min = 1;
		int startIndex = random.nextInt((max - min) + 1) + min;
		int lastIndex = startIndex + 3;
		for (int row = startIndex; row <= lastIndex; row++) {
			for (int col = startIndex; col <= lastIndex; col++) {
				Coordinate coordinate = new Coordinate();
				coordinate.x = row;
				coordinate.y = col;
				Tile tile = new Tile();
				tile.setLocation(coordinate);
				tiles.add(tile);
			}
		}
		board.setTiles(tiles);
		board.setSize(lastIndex);

		return board;
	}

}
