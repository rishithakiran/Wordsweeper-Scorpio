package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.xml.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game implements IModel {
	private String id;
	private boolean isLocked;
	private Board board;
	private ArrayList<Player> players = new ArrayList<>();
	private String password;


    /**
     * Notify all players in this game of a change by sending each a boardResponse message
     */
	public void notifyPlayers(){
        for (Player p : players) {
            String requestID = p.getClientState().id();
            BoardResponse br = new BoardResponse(p.getName(), this.getId(), requestID, false);
            Message brm = new Message(br.toXML());
            p.getClientState().sendMessage(brm);
        }
    }


	/**
	 * Notify all players in this game of a change by sending each a boardResponse message
	 * (Except the specified player :()
	 * @param playerName Player to ignore
	 */
	public void notifyEveryoneBut(String playerName){
		for (Player p : players) {
			if(p.getName().equals(playerName)){
				continue;
			}
			String requestID = p.getClientState().id();
			BoardResponse br = new BoardResponse(p.getName(), id, requestID, false);
			Message brm = new Message(br.toXML());
			p.getClientState().sendMessage(brm);
		}
	}

	/**
	 * Given a player, delete their entity from this game
	 * @param name The name of the player to delete
	 */
	public void deletePlayer(String name){
        this.players = (ArrayList<Player>) this.players.stream().filter(s -> !s.getName().equals(name)).collect(Collectors.toList());
    }


	/**
	 * Move a player within this game by the given amounts. This function enforces illegal moves,
	 * and will throw exception if the player tries to do something not allowed
	 * @param player The player to move
	 * @param colChange Delta in column. Must be between -1 and 1
	 * @param rowChange Delta in row. Must be between -1 and 1
	 * @throws WordSweeperException If the player attempts to move illegally or off the board
	 */
	public void repositionPlayer(String player, int colChange, int rowChange) throws WordSweeperException{
		// Verify that the proposed change is in bounds
		int playerBoardSize = 4;
		Player p = this.getPlayer(player);
		Coordinate currentLoc = p.getLocation();

		// Check right and bottom edges of the board
		if(currentLoc.col + playerBoardSize + colChange > this.getBoard().getSize() + 1 ||
				currentLoc.row + playerBoardSize + rowChange >= this.getBoard().getSize() + 1){
			throw new WordSweeperException("Player move out of bounds");
		}

		// Check top and left edges of the board
		if(currentLoc.col + colChange < 1 || currentLoc.row + rowChange < 1){
			throw new WordSweeperException("Player move out of bounds");
		}

		// Do the move
		p.setLocation(new Coordinate(currentLoc.col + colChange, currentLoc.row + rowChange));
	}


	// Complex Getters and Setters
	//=====================================================
	public Player getPlayer(String name){
		List<Player> l = this.players.stream().filter(s -> s.getName().equals(name)).collect(Collectors.toList());
		if(l.size() != 1){
			return null;
		}
		return l.get(0);
	}


	public Board getPlayerBoard(String player){
		Coordinate playerLoc = this.getPlayer(player).getLocation();
		return this.board.getSubBoard(playerLoc, 4);
	}

	public Player getManagingPlayer() {
		for (Player player : players) {
			if(player.isManagingUser()) {
                return player;
            }
		}
		return null;
	}


	// Simple Getters and Setters
	//=====================================================
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
	public void addPlayer(Player p){
		this.players.add(p);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Coordinate getBonus(){
		return this.board.getBonusLocation();
	}
}
