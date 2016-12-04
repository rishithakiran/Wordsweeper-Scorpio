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

	public void addPlayer(Player p){
		this.players.add(p);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Player getManagingPlayer() {
		for (Player player : players) {
			if (player.isManagingUser() == true)
				return player;
		}
		return null;
	}


    /**
     * Notify all players in this game of a change by sending each a boardResponse message
     */
	public void notifyPlayers(){
        for (Player p : players) {
            String requestID = p.getClientState().id();
            BoardResponse br = new BoardResponse(p.getName(), this.getId(), requestID, false);
            Message brm = new Message(br.toXML());
			System.out.println(brm);
            p.getClientState().sendMessage(brm);
        }
    }

	/**
	 * Notify all players in this game of a change by sending each a boardResponse message
	 */
	public void notifyEveryoneBut(String playerName){
		for (Player p : players) {
			if(p.getName().equals(playerName)){
				continue;
			}
			String requestID = p.getClientState().id();
			BoardResponse br = new BoardResponse(p.getName(), this.getId(), requestID, false);
			Message brm = new Message(br.toXML());
			System.out.println(brm);
			p.getClientState().sendMessage(brm);
		}
	}

	public Coordinate getBonus(){
		return this.board.getBonusLocation();
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

	public void repositionPlayer(String player, int colChange, int rowChange) throws WordSweeperException{
		// Verify that the proposed change is in bounds
		int playerBoardSize = 4;
		Player p = this.getPlayer(player);
		Coordinate currentLoc = p.getLocation();
		if(currentLoc.col + playerBoardSize + colChange > this.getBoard().getSize() ||
				currentLoc.row + playerBoardSize + rowChange > this.getBoard().getSize()){

			throw new WordSweeperException("Player move out of bounds");
		}

		// Do the move
		p.setLocation(new Coordinate(currentLoc.col + colChange, currentLoc.row + rowChange));
	}
}
