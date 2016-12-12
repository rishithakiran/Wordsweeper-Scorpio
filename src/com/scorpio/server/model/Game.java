package com.scorpio.server.model;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.serverbase.xml.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Includes all functionalities associated with the game entity class.
 * @author Saranya
 * @author Josh
 * @author Rishitha
 */
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


	// Complex Getters and Setters
		//=====================================================
		/**
		 * Gets the player associated with the game.
		 * @param name	Player name.
		 * @return	Player.
		 */
		public Player getPlayer(String name){
			List<Player> l = this.players.stream().filter((s) -> s.getName().equals(name)).collect(Collectors.toList());
			if(l.size() != 1){
				return null;
			}
			return l.get(0);
		}
		/**
		 * Returns a sub board with the associated player location.
		 * @param player Player name.
		 * @return	Sub Board for the player.
		 */
		public Board getPlayerBoard(String player){
			Coordinate playerLoc = this.getPlayer(player).getLocation();
			return this.board.getSubBoard(playerLoc, 4);
		}
		/**
		 * Checks which player is the managing user. 
		 * @return	player:	if managing user <p>null: if no managing user</p>
		 */
		public Player getManagingPlayer() {
			for (Player player : players) {
				if (player.isManagingUser() == true)
					return player;
			}
			return null;
		}

		// Simple Getters and Setters
		//=====================================================
		
		/**Getter method: Returns the ID of the game.
		 * @return	ID of the game.
		 **/
		public String getId() {
			return id;
		}
		/**
		 * Setter method: Assigns an ID with the game.
		 * @param id 	ID of the game that is to be assigned. 
		 **/
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * Checks whether the game is locked or not.
		 * @return	Appropriate boolean value.
		 */
		public boolean isLocked() {
			return isLocked;
		}
		/**
		 * Setter method: Assigns the lock vale for the game.
		 * @param isLocked	true/false for game lock state.
		 */
		public void setLocked(boolean isLocked) {
			this.isLocked = isLocked;
		}
		/** 
		 * Getter method: Returns the board created for the required size.
		 * @return	Board of required size. 
		 */
		public Board getBoard() {
			return board;
		}
		/**
		 * Setter method: Sets the board to the game.
		 * @param board Associates board to the game. 
		 **/
		public void setBoard(Board board) {
			this.board = board;
		}
		/**
		 * Returns a list of players in the game.
		 * @return	Players of the game.
		 */
		public ArrayList<Player> getPlayers() {
			return players;
		}
		/**
		 * Adds each player to the game.
		 * @param p	Player to be added to the game.
		 */
		public void addPlayer(Player p){
			this.players.add(p);
		}
		/**
		 * Getter method: Returns the password associated with the game.
		 * @return	Password of the game.
		 */
		public String getPassword() {
			return password;
		}
		/**
		 * Setter method: Assigns the password to the game.
		 * @param password	Password given for the game.
		 */
		public void setPassword(String password) {
			this.password = password;
		}
		/**
		 * Getter method: Returns the bonus point associated with the tile.
		 * @return	The location of the bonus point.
		 */
		public Coordinate getBonus(){
			return this.getBoard().getBonusLocation();
		}
}