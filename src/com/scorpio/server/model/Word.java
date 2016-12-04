package com.scorpio.server.model;
import com.scorpio.server.exception.WordSweeperException;

import java.util.ArrayList;
/**
 * Includes functionalities for Word class.
 * @author Saranya, Josh, Rishitha
 *
 */
public class Word implements IModel{
    public ArrayList<Tile> tiles;

    /**
     * Constructor of Word class.
     * @param tiles Array List of tiles.
     */
	public Word(ArrayList<Tile> tiles) {
		super();
		this.tiles = tiles;
	}
	/**
	 * Serializes the tiles.
	 */
	public String toString(){
		String s = "";
		for(Tile t : this.tiles){
			s += t.toString();
		}
		return s;
	}
	/**Returns boolean values of whether a tile has bonus value or not*/
	public boolean hasBonus(){
		for(Tile t : this.tiles){
			if(t.isBonus()){
				return true;
			}
		}
		return false;
	}
	/**
	 * Calculates the value for input word.
	 * @return	word score
	 * @throws WordSweeperException	Throws appropriate Word Sweeper Exception.
	 */
	public int computeScore() throws WordSweeperException{
		if(tiles == null){
			return 0;
		}
		if(tiles.size()<3) {
			throw new WordSweeperException("Word too short");
		}

		int word_score = 0;
		for (Tile tile : tiles) {
			int m = tile.getSharedBy();
			int tile_score = tile.getPoints();
			if(tile.isBonus()){
				tile_score *= 10;
			}
			if(m > 1) {
				tile_score *= (int) Math.pow(2, m);
			}

			word_score += tile_score;
		}

		return (int)Math.pow(2,tiles.size()) * 10 * word_score;

	}
}
