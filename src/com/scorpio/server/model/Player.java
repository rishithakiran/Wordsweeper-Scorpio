package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.xml.Message;

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

    public ClientState getClientState(){
		return this.state;
	}

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
