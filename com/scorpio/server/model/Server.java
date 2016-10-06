package com.scorpio.server.model;

import java.util.HashMap;

public class Server {
	private HashMap<Integer, Game> games = new HashMap<Integer, Game>();

	public Server(){

	}

	public Game findGameById(int gameId){
		return this.games.get(gameId);
	}
}
