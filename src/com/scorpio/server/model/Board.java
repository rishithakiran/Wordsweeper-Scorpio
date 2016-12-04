package com.scorpio.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.accessory.Dictionary;
import com.scorpio.server.exception.WordSweeperException;

public class Board implements IModel {

	protected ArrayList<Tile> tiles;
	protected int size;

    private Coordinate bonusCoord;

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
		if(size + start.row > this.size + 1 || size + start.col > this.size + 1){
			return null;
		}
		if(start.row < 1 || start.col < 1){
			return null;
		}

		Board subBoard = new Board(size);
		for(int col = 0; col < size; col++){
			for(int row = 0; row < size; row++){
				Tile t = this.getTileAt(new Coordinate(start.col + col, start.row + row));
				// Make a fresh copy of this tile
				// This is inelegant, but I can't think of a prettier way to handle
				// the issue
				Tile tCopy = new Tile();
				tCopy.setContents(t.getContents());
				tCopy.setBonus(t.isBonus());
				// Readdress the coordinate
				tCopy.setLocation(new Coordinate(col+1,row+1));
				subBoard.tiles.add(tCopy);
			}
		}
		return subBoard;
	}

    /**
     * Selects a tile randomly from this board and marks it as a bonus tile. If
     * there is already a bonus tile, it will clear it first.
     */
	public void addBonus(){
        if(this.bonusCoord != null){
            this.getTileAt(this.bonusCoord).setBonus(false);
        }

        Random random = new Random();
        int col = random.nextInt(this.getSize()) + 1;
        int row = random.nextInt(this.getSize()) + 1;
        Coordinate c = new Coordinate(col, row);
        this.getTileAt(c).setBonus(true);
        this.bonusCoord = c;
    }

    /**
     * Try to use the cached value of the location of the bonus on this board.
     * However, if it fails checks, getBonusLocation() will do its best to find
     * the bonus tile if it exists, or add one if there is no bonus tile on the
     * board
     * @return Coordinate location of the bonus tile on this board
     */
    public Coordinate getBonusLocation(){
        if(this.bonusCoord == null){
            this.addBonus();
            return this.getBonusLocation();
        }

        /* This is really bad, so hopefully it never gets called
         * If it is, that means that the board somehow got out of
         * sync with itself, and the stored location on the bonus
         * doesn't match up with where it actually exists (assuming
         * it even exists at all). So we need to scrub through the
         * board to either find where the bonus really is, or if it
         * doesn't exist anymore, add a new one
         * While we're doing this anyway, we'll check to ensure that
         * there's only one bonus
         */
        if(this.getTileAt(this.bonusCoord).isBonus()) {
            int bonusesFound = 0;
            for (int col = 1; col <= this.size; ++col) {
                for (int row = 1; row <= this.size; ++row) {
                    if (this.getTileAt(new Coordinate(col, row)).isBonus()) {
                        bonusesFound++;
                        if(bonusesFound == 1) {
                            this.bonusCoord = new Coordinate(col, row);
                        }
                    }
                }
            }
            if(bonusesFound == 0){
                this.addBonus();
                return this.getBonusLocation();
            }
            if(bonusesFound > 1){
                // We really messed up now. We'll need to reset the bonus tiles on the
                // board and keep the first one we found, as that's the one that we've
                // been sending to clients. They shouldn't notice
                for (int col = 1; col <= this.size; ++col) {
                    for (int row = 1; row <= this.size; ++row) {
                        if(!this.bonusCoord.equals(new Coordinate(col, row))) {
                            this.getTileAt(new Coordinate(col, row)).setBonus(false);
                        }
                    }
                }
            }
        }

        return this.bonusCoord;
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


	/**
	 * Serialize the board in preparation to be sent through a boardResponse message
	 * @return Board represented as a comma separated string
	 */
	public String toString(){
		String out = "";
		for(int row = 1; row <= this.size; row++){
			for(int col = 1; col <= this.size; col++){
                Tile t = this.getTileAt(new Coordinate(col,row));
                if(t != null) {
                    out += t.getContents() + ",";
                }
			}
		}
		// Strip the trailing comma
		out = out.substring(0, out.length() - 1);
		return out;
	}

    /**
     * Given a Word object, ensure that the tiles contained within it exist
     * on this board. Additionally, this checks to ensure that the Word is in
     * fact an English word, and that all tiles are adjacent to one another
     * in the order they appear
     * @param w The Word object to check
     * @return true if the word exists and is valid, false otherwise
     */
	public boolean hasWord(Word w) {
		/* We affirm that the tiles included in w match with what is described
		 * by this board state. Additionally, we consult the dictionary to ensure
		 * that the word is real. Finally, we ensure that each tile is adjacent to
		 * the previous one.
		 */
		for(int i = 0; i <  w.tiles.size(); ++i){
            Tile t = w.tiles.get(i);
			if(t.getLocation().col > this.size || t.getLocation().col < 1 ||
					t.getLocation().row > this.size || t.getLocation().row < 1){
				return false;
			}
			Tile trueTile = this.getTileAt(t.getLocation());
			if(!trueTile.equals(t)){
				return false;
			}

			// Ensure adjacency
			if(i > 0){
                Coordinate priorCoords = w.tiles.get(i-1).getLocation();
                Coordinate currentCoords = w.tiles.get(i).getLocation();

                int xdiff = Math.abs(priorCoords.row - currentCoords.row);
                int ydiff = Math.abs(priorCoords.col - currentCoords.col);

                if(xdiff > 1 || ydiff > 1){
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
		Board newBoard = new RandomBoard(newsize);
		// Create the required new tiles
		for(int col = this.size; col < this.size; col++){
			for(int row = this.size; row < this.size; row++){
				Tile t = this.getTileAt(new Coordinate(col, row));
				newBoard.setTileAt(new Coordinate(col, row), t);
			}
		}
		this.tiles = newBoard.tiles;
		this.size = newsize;
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

        for(int col = 1; col <= this.size; ++col){
            int offset = 0;
            for(int row = 1; row <= this.size; ++row){
                // Read all tiles not marked for deletion
                Tile t = this.getTileAt(new Coordinate(col,row));
                if(t.markedForDelete){
                    ++offset;
                }else{
                    t.setLocation(new Coordinate(t.getLocation().col, t.getLocation().row - offset));
                    rebuiltBoard.tiles.add(t);
                }
            }
            // Populate with new random tiles
            for(int row = this.getSize() - offset + 1; row <= this.size; ++row){
                Tile newTile = new Tile();
                newTile.setLocation(new Coordinate(col, row));
                rebuiltBoard.tiles.add(newTile);
            }
        }

        this.tiles = rebuiltBoard.tiles;
    }

}
