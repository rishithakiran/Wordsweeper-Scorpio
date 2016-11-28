package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.exception.WordSweeperException;

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
		int total_score=0;
		ArrayList<Tile> tiles = word.tiles;
		for (Tile tile : tiles) {
			int m=tile.getSharedBy();
			tile_score = (tile.getPoints() * tile.getMultiplier()) *(int)Math.pow(2, m);
			word_score = word_score + tile_score;
		}
		if(tiles.size()>3){
			total_score=(int)Math.pow(2,tiles.size())*10*word_score;
			return total_score;
		}
		else
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

	public Player getPlayer(String name){
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

	public void repositionPlayer(String player, int rowChange, int colChange) throws WordSweeperException{
		// Verify that the proposed change is in bounds
		int playerBoardSize = 4;
		Player p = this.getPlayer(player);
		Coordinate currentLoc = p.getLocation();
		if(currentLoc.y + playerBoardSize + rowChange >= this.getBoard().getSize() ||
				currentLoc.x + playerBoardSize + colChange >= this.getBoard().getSize()){

			throw new WordSweeperException("Player move out of bounds");
		}

		// Do the move
		p.setLocation(new Coordinate(currentLoc.x + colChange, currentLoc.y + rowChange));
	}
}
