package com.scorpio.server.core;

import com.scorpio.server.model.Game;
import com.scorpio.server.model.IModel;

import java.util.HashMap;

/**
	The server model is a singleton
 */
public class GameManager implements IModel {
	public HashMap<Integer, Game> games = new HashMap<Integer, Game>();
	private static GameManager instance = null;

	public static GameManager getInstance(){
		if(instance == null){
			instance = new GameManager();
		}
		return instance;
	}
	private GameManager(){

	}

	public int numberOfGames(){
		return games.size();
	}

	public Game findGameById(int gameId){
		return this.games.get(gameId);
	}
}
