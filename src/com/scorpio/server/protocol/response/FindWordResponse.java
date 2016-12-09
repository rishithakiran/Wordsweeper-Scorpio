package com.scorpio.server.protocol.response;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

import java.util.ArrayList;
import java.util.List;
/**
 * The Find word Response holds all functionalities with respect to the corresponding 
 * FindWord requests, necessary to send to the client. 
 * @author Saranya
 * @author Josh
 *
 */
public class FindWordResponse {
    private final String playerID;
    private final String gameID;
    private final String requestID;
    private long score;
    private final String error;
    
    /**
     * Constructor for FindWord response that handles playerId, gameId, requestId and score.
     * @param playerID		ID of the player requested for finding word.
     * @param gameID		ID of the game playing.
     * @param requestID		Request ID.
     * @param score			Score calculated for valid word.
     */
    public FindWordResponse(String playerID, String gameID, String requestID, int score){
        this.playerID = playerID;
        this.gameID = gameID;
        this.requestID = requestID;
        this.score = score;
        this.error = null;
    }
    
    /**
     * Constructor for FindWord response that handles playerId, gameId, requestId and error.
     * @param playerID		ID of the player requested for finding word.
     * @param gameID		ID of the game playing.
     * @param requestID		Request ID.
     * @param error			Error message for invalid word.
     */
    public FindWordResponse(String playerID, String gameID, String requestID, String error){
        this.playerID = playerID;
        this.gameID = gameID;
        this.requestID = requestID;
        this.score = 0;
        this.error = error;
    }

    /**
     * Create standard response XML header string for successful response or failure response.
	 * And create a FindWord response XML header with gameId and score.
	 * @return	Header of the response.
     */
    private String getHeader(){
        Game g = GameManager.getInstance().findGameById(gameID);

        String managingUser = g.getManagingPlayer().getName();
        String header;
        if(this.error != null){
            header = "<response id='" + requestID + "' success='false' reason='" + this.error +"'>";
        }else {
            header = "<response id='" + requestID + "' success='true'>";
        }
        String boardResponseHeader = String.format(
                "<findWordResponse gameId='%s' name='%s' score='%d'>",
                gameID, managingUser, this.score
        );

        return header + boardResponseHeader;
    }

    /**
     * Create standard response XML footer string for Find Word response.
	 * @return	Footer of the response.
	 */
    private String getFooter(){
        String boardResponseFooter = "</findWordResponse>";
        String footer = "</response>";

        return boardResponseFooter + footer;
    }

    /**
     * The appropriate data is returned to the client.
	 * @return Header and Footer.
	 */
    public String toXML(){
        String complete = this.getHeader();

        complete += this.getFooter();

        // We will return the appropriate data to the client
        return complete;
    }
}
