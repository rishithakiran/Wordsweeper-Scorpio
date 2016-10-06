package com.scorpio.server.model;

import java.util.ArrayList;

public class Game implements IModel{
    private int id;
    private boolean isLocked;
    private Board board;
    private ArrayList<Player> players;
    private String password;

    public int computeScore(){
        return 0;
    }

    public Player getManagingPlayer(){
        return null;
    }
}
