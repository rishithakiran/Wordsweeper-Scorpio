package com.scorpio.server.model;

import java.util.ArrayList;

public class Word implements IModel{
    ArrayList<Tile> tiles;

	public Word(ArrayList<Tile> tiles) {
		super();
		this.tiles = tiles;
	}
}
