package com.scorpio.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;

public class Board implements IModel {

	private ArrayList<Tile> tiles;
	private int size;

	public ArrayList<Tile> getTiles() {
		return tiles;
	}


	/**
	 * Get a subboard that grows along these coordinates
	 *  --> X+
	 * |
	 * V
	 * Y+
	 */

	public Board getSubBoard(Coordinate start, int size){
		Board subBoard = new Board(4);
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				subBoard.tiles.add(this.getTileAt(new Coordinate(start.x + x, start.y + y)));
			}
		}
		return subBoard;
	}


	public Tile getTileAt(Coordinate c){
		List<Tile> l = this.tiles.stream().filter((t) -> t.getLocation().equals(c)).collect(Collectors.toList());
		if(l.size() != 1){
			return null;
		}
		return l.get(0);
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

	public Board(int size) {
		this.tiles = new ArrayList<Tile>();
		this.size = size;
	}

	public void fillRandom(){
		for (int row = 0; row < this.size; row++) {
			for (int column = 0; column < this.size; column++) {
				Coordinate coordinate = new Coordinate(row, column);
				Tile tile = new Tile();
				tile.setLocation(coordinate);
				tiles.add(tile);
			}
		}
	}

	// Serialize the board to a format that can be passed
	// directly to an XML document
	public String toString(){
		String out = "";
		for(int x = 0; x < this.size; x++){
			for(int y = 0; y < this.size; y++){
				out += this.getTileAt(new Coordinate(x,y)).getContents() + ",";
			}
		}
		// Strip the trailing comma
		out = out.substring(0, out.length() - 1);
		return out;
	}


	public boolean isValidWord(Word w) {
		return false;
	}

	public Board grow(Board board, int size) {

		ArrayList<Tile> tiles = new ArrayList<>();
		Random random = new Random();
		int max = board.getSize() + size;
		Board newBoard = new Board(4);
		for (Tile tile : board.getTiles()) {
			tiles.add(tile);
		}
		// the first board in the game will be created at position 0
		int min = 1;
		int startIndex = random.nextInt((max - min) + 1) + min;
		int lastIndex = startIndex + 3;
		for (int row = startIndex; row <= lastIndex; row++) {
			for (int col = startIndex; col <= lastIndex; col++) {
				Coordinate coordinate = new Coordinate(row,col);
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
