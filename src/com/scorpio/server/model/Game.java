package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Game implements IModel {
	private String id;
	private boolean isLocked;
	private Board board;
	private ArrayList<Player> players;
	private String password;

	public Game(){
		this.players = new ArrayList<Player>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public void addPlayer(Player p){
		this.players.add(p);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int computeScore(String player, Word word) {
		int tile_score = 0;
		int word_score = 0;
		ArrayList<Tile> tiles = word.tiles;
		for (Tile tile : tiles) {
			tile_score = (tile.getPoints() * tile.getMultiplier()) * tile.getSharedBy();
			word_score = word_score + tile_score;
		}
		return word_score;
	}

	public Player getManagingPlayer() {
		for (Player player : players) {
			if (player.isManagingUser() == true)
				return player;
		}
		return null;
	}


	public Coordinate getBonus(){
		return new Coordinate(0,0);
	}

	private Player getPlayer(String name){
		List<Player> l = this.players.stream().filter((s) -> s.getName().equals(name)).collect(Collectors.toList());
		if(l.size() != 1){
			return null;
		}
		return l.get(0);
	}
	public Board getPlayerBoard(String player){
		Coordinate playerLoc = this.getPlayer(player).getLocation();
		return this.board.getSubBoard(playerLoc, 4);
	}

	public void deletePlayer(String name){
        this.players = (ArrayList<Player>) this.players.stream().filter((s) -> !s.getName().equals(name)).collect(Collectors.toList());
    }

}
