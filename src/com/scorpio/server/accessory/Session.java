package com.scorpio.server.accessory;


import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

/**
 * Represents a user's presence on this server by tying together a user and a game.
 * In many controllers, we attach this information to the client state, and use it to
 * validate that requests came from the user they claim to have come from. Additionally,
 * these are used to cleanup on a logout event
 * @author Josh
 */
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
