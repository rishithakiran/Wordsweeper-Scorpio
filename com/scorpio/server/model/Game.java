package com.scorpio.server.model;

import java.util.ArrayList;

public class Game implements IModel {

	private int id;
	private boolean isLocked;
	private Board board;
	private ArrayList<Player> players;
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int computeScore() {
		return 0;
	}

	public Player getManagingPlayer() {
		return null;
	}
}
