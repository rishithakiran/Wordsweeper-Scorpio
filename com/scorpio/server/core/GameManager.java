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
	public HashMap<UUID, Game> games = new HashMap<UUID, Game>();
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

	public Game findGameById(UUID gameId){
		return this.games.get(gameId);
	}
}
