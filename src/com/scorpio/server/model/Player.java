package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;

public class Player implements IModel{
	private String name;
    private Coordinate location;
    private int score;
    private boolean isManagingUser;

	// Maintain the client state, so we can send this user messages
	private ClientState state;


    public Player(String name, ClientState s){
		this.state = s;
		this.name = name;
		this.score = 0;
    }

	/**
	 * Evaluate if a given tile is on this Player's board
	 * @param t Tile to test
	 * @return true if the Tile is on the Player's board, false otherwise
	 */
	public boolean hasTile(Tile t){
		int pcol = this.getLocation().col;
		int prow = this.getLocation().row;

		int tcol = t.getLocation().col;
		int trow = t.getLocation().row;
		if(tcol - pcol >= 0 && tcol - pcol <= 3 &&
				trow - prow >= 0 && trow - prow <=3){
			return true;
		}
		return false;
	}


	// Simple Getters and Setters
	//=====================================================
	public ClientState getClientState(){
		return this.state;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public Coordinate getLocation() {
		return location;
	}
	public void setLocation(Coordinate location) {
		this.location = location;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isManagingUser() {
		return isManagingUser;
	}
	public void setManagingUser(boolean isManagingUser) {
		this.isManagingUser = isManagingUser;
	}
}
