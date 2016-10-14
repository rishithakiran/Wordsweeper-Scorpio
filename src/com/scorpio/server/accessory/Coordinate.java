package com.scorpio.server.accessory;

public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return String.format("%d,%d", x, y);
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Coordinate))return false;
        Coordinate otherCoordinate = (Coordinate) other;
        if(otherCoordinate.x == this.x && otherCoordinate.y == this.y){
            return true;
        }
        return false;
    }
}

