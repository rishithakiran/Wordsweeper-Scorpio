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

	public Board(int size) {
		this.tiles = new ArrayList<Tile>();
		this.size = size;
	}


	/**
	 * Get a square board that is a subset of this board
	 * @param start Location of the top-left corner of the sub board
	 * @param size Size of the sub board (it's a square)
	 * @return The sub board
	 */
	public Board getSubBoard(Coordinate start, int size){
		// TODO Bounds check

		Board subBoard = new Board(4);
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				subBoard.tiles.add(this.getTileAt(new Coordinate(start.x + x, start.y + y)));
			}
		}
		return subBoard;
	}


	/**
	 * Given a coordinate, look at the board associated with this game an identify the
	 * tile there
	 * @param c The coordinate to use for lookup
	 * @return Tile at that location
	 */
	public Tile getTileAt(Coordinate c){
		List<Tile> l = this.tiles.stream().filter((t) -> t.getLocation().equals(c)).collect(Collectors.toList());
		if(l.size() != 1){
			return null;
		}
		return l.get(0);
	}

	/**
	 * @return Current height/width of the board (it's a square)
	 */
	public int getSize() {
		return size;
	}


	/**
	 * When called, will fill all tiles on the board with a random
	 * value
	 */
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

	/**
	 * Serialize the board in preparation to be sent through a boardResponse message
	 * @return Board represented as a comma separated string
	 */
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


	/**
	 * Grows the board to match the given size. The board will always be
	 * a square. If this new size is smaller than the current board, there
	 * will be no effect
	 * @param newsize The new size to grow to
	 */
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
