package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;

/**
 * Includes all functionalities associated to the player entity class.
 * @author Saranya
 * @author Josh
 * @author Rishitha
 */

public class Player implements IModel{
	private String name;
    private Coordinate location;
    private int score;
    private boolean isManagingUser;

	// Maintain the client state, so we can send this user messages
	private ClientState state;
	/**
	 * Construct a player class with name and client state. 
	 * @param name	Name of the player.
	 * @param s	Client State.
	 */
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
    /**
     * Getter method: Returns the Client state.
     * @return	Client State.
     */
    public ClientState getClientState(){
		return this.state;
	}
    /**
     * Getter method: Returns the name/id of the player.
     * @return	Name/ID of the player.
     */
    public String getName() {
		return name;
	}
    /**
     * Setter method: Assigns the name/ID to the player.
     * @param name	Name/ID of the player.
     */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Getter method: Returns the location of the player in the board.
	 * @return	Location of the player.
	 */
	public Coordinate getLocation() {
		return location;
	}
	/**
	 * Setter method: Assigns the location of the player in the board.
	 * @param location	Coordinate location (col,row).
	 */
	public void setLocation(Coordinate location) {
		this.location = location;
	}
	/**
	 * Getter method: The calculated score of the player is returned.
	 * @return	Score of the player.
	 */
	public int getScore() {
		return score;
	}
	/**
	 * Setter method: Calculates the score for the words and assigns to the player.
	 * @param score	The Calculated score.
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * Checks whether the player is the managing user.
	 * @return	Appropriate boolean value.
	 */
	public boolean isManagingUser() {
		return isManagingUser;
	}
	/**
	 * Setter method: Assigns a boolean value for the player, if the player
	 * is managing user or not.
	 * @param isManagingUser	true/false for setting up the managing user.
	 */
	public void setManagingUser(boolean isManagingUser) {
		this.isManagingUser = isManagingUser;
	}
}
