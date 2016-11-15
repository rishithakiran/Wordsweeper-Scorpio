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

}
