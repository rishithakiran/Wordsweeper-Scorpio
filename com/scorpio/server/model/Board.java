package com.scorpio.server.model;

import java.util.ArrayList;

public class Board implements IModel{
    private ArrayList<Tile> tiles;
    private int size;

    public Board(){
        // Board starts at size 7
        this.size = 7;
    }

    private void grow(int size){
        return;
    }

    public boolean isValidWord(Word w){
        return false;
    }
}
