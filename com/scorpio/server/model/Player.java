package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;

public class Player implements IModel{
	private String name;
    private Coordinate location;
    private int score;
    private boolean isManagingUser;
    
    public Player(){

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
