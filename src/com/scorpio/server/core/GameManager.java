package com.scorpio.server.core;

import com.scorpio.server.model.Game;
import com.scorpio.server.model.IModel;
import com.scorpio.server.model.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * The Game Manager is the server model which is singleton.
 * @author Josh
 */
public class GameManager implements IModel {
	
	/**Creates HashMap that stores all details about the games.*/
	public HashMap<String, Game> games = new HashMap<String, Game>();
	private static GameManager instance = null;

	/**
	 * Creates new instance for Game Manager.
	 */
	public static void reset(){
		instance = new GameManager();
	}


	public static GameManager getInstance(){
		if(instance == null){
			instance = new GameManager();
		}
		return instance;
	}
	/**
	 * Deletes the game associated with the given gameID
	 * @param gameId	The ID of the game to be removed.
	 **/
	public void deleteGame(String gameId){
		this.games.remove(gameId);
	}
	/**
	 * Returns the total number of active games.
	 * @return	number of games.
	 */
	public int numberOfGames(){
		return games.size();
	}
	/**
	 * Finds the details associated to the game ID.
	 * @param gameId	ID of the game
	 * @return	details of the game.
	 */
	public Game findGameById(String gameId){
		return this.games.get(gameId);
	}
}
