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
 * @author Josh
 * @author Apoorva
 *
 */
public class JoinGameResponse {

    private final String requestID;
    private final String error;
    private final String gameId;

    /**
     * Constructor of the JoinGame response that handles gameId, requestId and error.
     * @param gameId	ID of the game to be joined by the player.
     * @param requestID	Request ID.
     * @param error		Error message if unable to join.
     */
    public JoinGameResponse(String gameId, String requestID, String error){
        this.gameId = gameId;
        this.requestID = requestID;
        this.error = error;
    }
    
    /**
     * Create standard response XML header string for successful response or failure response.
	 * And create a JoinGame response XML header with gameId.
	 * @return	Header of the response.
     */
    private String getHeader(){
        String header;
        if(this.error != null){
            header = "<response id='" + requestID + "' success='false' reason='" + this.error +"'>";
        }else {
            header = "<response id='" + requestID + "' success='true'>";
        }
        String boardResponseHeader = String.format("<joinGameResponse gameId='%s'/>", gameId);

        return header + boardResponseHeader;
    }

    /**
     * Create standard response XML footer string for Join Game response.
	 * @return	Footer of the response.
	 */
    private String getFooter(){
        String footer = "</response>";
        return footer;
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
