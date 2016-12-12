package com.scorpio.server.accessory;


import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

public class Session {
    public Game g;
    public Player p;

    public Session(Player p, Game g){
        this.p = p;
        this.g = g;
    }

    public Player getPlayer(){
        return this.p;
    }

    public Game getGame(){
        return this.g;
    }
}
