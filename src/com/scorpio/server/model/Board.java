package com.scorpio.server.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.accessory.Dictionary;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;

public class Board implements IModel {

	protected ArrayList<Tile> tiles;
	protected int size;

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
		if(size + start.x > this.size || size + start.y > this.size){
			return null;
		}

		Board subBoard = new Board(size);
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				Tile t = this.getTileAt(new Coordinate(start.x + x, start.y + y));
				// Make a fresh copy of this tile
				// This is inelegant, but I can't think of a prettier way to handle
				// the issue
				Tile tCopy = new Tile();
				tCopy.setContents(t.getContents());
				tCopy.setPoints(t.getPoints());
				tCopy.setMultiplier(t.getMultiplier());
				// Readdress the coordinate
				tCopy.setLocation(new Coordinate(x,y));
				subBoard.tiles.add(tCopy);
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

	public void setTileAt(Coordinate c, Tile newTile){
        List<Tile> l = this.tiles.stream().filter((t) -> !t.getLocation().equals(c)).collect(Collectors.toList());
        if(l.size() != (this.getSize() * this.getSize()) - 1){
            return;
        }
        this.tiles = new ArrayList<>(l);
        if(newTile != null) {
            this.tiles.add(newTile);
        }
    }

	/**
	 * @return Current height/width of the board (it's a square)
	 */
	public int getSize() {
		return size;
	}



	public void pp(){
        for(int y = 0; y < this.size; y++){
            for(int x = 0; x < this.size; x++){
                Tile t = this.getTileAt(new Coordinate(x,y));
                if(t != null) {
                    System.out.print(t.getContents());
                }
            }
            System.out.println();
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
                Tile t = this.getTileAt(new Coordinate(x,y));
                if(t != null) {
                    out += t.getContents() + ",";
                }
			}
		}
		// Strip the trailing comma
		out = out.substring(0, out.length() - 1);
		return out;
	}

	public boolean hasWord(Word w) {
		/* We affirm that the tiles included in w match with what is described
		 * by this board state. Additionally, we consult the dictionary to ensure
		 * that the word is real. Finally, we ensure that each tile is adjacent to
		 * the previous one.
		 */
		for(int i = 0; i <  w.tiles.size(); ++i){
            Tile t = w.tiles.get(i);
			Tile trueTile = this.getTileAt(t.getLocation());
			if(!trueTile.equals(t)){
				return false;
			}

			// Ensure adjacency
			if(i > 0){
                Coordinate priorCoords = w.tiles.get(i-1).getLocation();
                Coordinate currentCoords = w.tiles.get(i).getLocation();

                int xdiff = Math.abs(priorCoords.x - currentCoords.x);
                int ydiff = Math.abs(priorCoords.y - currentCoords.y);

                if(xdiff + ydiff != 1){
                    return false;
                }
            }
		}

		return Dictionary.getInstance().isWord(w.toString().toLowerCase());
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


    /**
     * After confirming that the given word exists in this board,
     * remove it from the board and update surrounding tiles
     * accordingly
     * @param w The word to remove from the board
     */
	public void removeWord(Word w) throws WordSweeperException{
        if(!this.hasWord(w)){
            throw new WordSweeperException("Word does not exist");
        }


        for(Tile t : w.tiles){
            this.getTileAt(t.getLocation()).markedForDelete = true;
        }

        Board rebuiltBoard = new Board(this.size);

        for(int x = 0; x < this.size; ++x){
            int offset = 0;
            for(int y = 0; y < this.size; ++y){
                // Read all tiles not marked for deletion
                Tile t = this.getTileAt(new Coordinate(x,y));
                if(t.markedForDelete){
                    ++offset;
                }else{
                    t.setLocation(new Coordinate(t.getLocation().x, t.getLocation().y - offset));
                    rebuiltBoard.tiles.add(t);
                }
            }
            // Populate with new random tiles
            for(int y = this.getSize() - offset; y < this.size; ++y){
                Tile newTile = new Tile();
                newTile.setLocation(new Coordinate(x, y));
                rebuiltBoard.tiles.add(newTile);
            }
        }

        this.tiles = rebuiltBoard.tiles;
    }

}
