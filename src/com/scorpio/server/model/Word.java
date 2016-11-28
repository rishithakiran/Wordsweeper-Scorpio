package com.scorpio.server.model;
import java.util.ArrayList;

public class Word implements IModel{
    public ArrayList<Tile> tiles;

	public Word(ArrayList<Tile> tiles) {
		super();
		this.tiles = tiles;
	}

	public String toString(){
		String s = "";
		for(Tile t : this.tiles){
			s += t.toString();
		}
		return s;
	}


	public int computeScore() {
		if(tiles == null){
			return 0;
		}
		if(tiles.size()<3) {
			return 0;
		}

		int word_score = 0;
		for (Tile tile : tiles) {
			int m=tile.getSharedBy();
			int tile_score = (tile.getPoints() * tile.getMultiplier());
			if(m > 1) {
				tile_score *= (int) Math.pow(2, m);
			}

			word_score += tile_score;
		}

		return (int)Math.pow(2,tiles.size()) * 10 * word_score;

	}
}
