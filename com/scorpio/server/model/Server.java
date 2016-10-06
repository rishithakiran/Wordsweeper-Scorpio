package com.scorpio.server.model;

import java.util.HashMap;

/**
	The server model is a singleton
 */
public class Server {
	private HashMap<Integer, Game> games = new HashMap<Integer, Game>();
	private Server instance = null;

	public Server getInstance(){
		if(instance == null){
			instance = new Server();
		}
		return instance;
	}
	private Server(){

	}

	public Game findGameById(int gameId){
		return this.games.get(gameId);
	}
}
