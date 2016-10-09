package com.scorpio.server.model;

import java.util.HashMap;

/**
	The server model is a singleton
 */
public class GameManager implements IModel{
	private HashMap<Integer, Game> games = new HashMap<Integer, Game>();
	private static GameManager instance = null;

	public static GameManager getInstance(){
		if(instance == null){
			instance = new GameManager();
		}
		return instance;
	}
	private GameManager(){

	}

	public Game findGameById(int gameId){
		return this.games.get(gameId);
	}
}
