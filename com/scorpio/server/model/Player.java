package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;

public class Player implements IModel{
    private String name;
    private Coordinate location;
    private int score;
    private boolean isManagingUser;

    public Player(){

    }

}
