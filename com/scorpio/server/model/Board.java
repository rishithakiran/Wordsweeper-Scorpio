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

	public int getSize() {
		return size;
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
				this.tiles.add(tile);
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

	public void grow(int newsize) {
		if(this.size >= newsize){
			return;
		}

		// Create the required new tiles
		for(int x = this.size; x < newsize; x++){
			for(int y = this.size; y < newsize; y++){
				Tile t = new Tile();
				t.setLocation(new Coordinate(x, y));
				this.tiles.add(t);
			}
		}

	}

}
