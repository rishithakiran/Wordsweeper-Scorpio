package com.scorpio.server.accessory;

public class Coordinate {
    public int row;
    public int col;


    public Coordinate(int col, int row){
        this.row = row;
        this.col = col;
    }


    public String toString(){
        return String.format("%d,%d", col, row);
    }


    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Coordinate))return false;
        Coordinate otherCoordinate = (Coordinate) other;
        if(otherCoordinate.row == this.row && otherCoordinate.col == this.col){
            return true;
        }
        return false;
    }
}

