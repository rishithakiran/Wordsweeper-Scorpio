package com.scorpio.server.core;

import com.scorpio.server.model.Game;
import com.scorpio.server.model.IModel;
import com.scorpio.server.model.Player;

import java.util.HashMap;
import java.util.UUID;

/**
	The server model is a singleton
 */
public class GameManager implements IModel {
	

	public HashMap<String, Game> games = new HashMap<String, Game>();
	private static GameManager instance = null;

	public static void reset(){
		instance = new GameManager();
	}

	public static GameManager getInstance(){
		if(instance == null){
			instance = new GameManager();
		}
		return instance;
	}
	private GameManager(){

	}

	public void deleteGame(String gameId){
		this.games.remove(gameId);
	}
	public int numberOfGames(){
		return games.size();
	}

	public Game findGameById(String gameId){
		return this.games.get(gameId);
	}
	
	
}
