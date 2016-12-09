package com.scorpio.server.protocol.response;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

import java.util.ArrayList;
import java.util.List;
/**
 * The Reset game Response holds all functionalities with respect to the corresponding 
 * Reset game requests, necessary to send to the client. 
 * @author Josh
 *
 */
public class ResetGameResponse {
    private final String gameID;
    private final String requestID;
    private final String failure;
    
    /**
     * Constructor for Reset game response that handles gameID, requestID and failure.
     * @param gameID	ID of the game to be reset.
	 * @param requestID	Request ID.
	 * @param failure	Failure message if unable to rest.
     */
    public ResetGameResponse(String gameID, String requestID, String failure){
        this.gameID = gameID;
        this.requestID = requestID;
        this.failure = failure;
    }

    /**
     * Constructor for Reset game response that handles gameID and requestID.
     * @param gameID	ID of the game to be reset.
	 * @param requestID	Request ID.
	 */
    public ResetGameResponse(String gameID, String requestID){
        this.gameID = gameID;
        this.requestID = requestID;
        this.failure = null;
    }

    /**
	 * Create standard response XML header string for successful response or failure response.
	 * And create a reset game response with gameId.
	 * @return	Header of the response.
	 */
    private String getHeader(){
        final String header;
        if(this.failure != null){
            header = "<response id='" + requestID + "' success='false' reason='" + this.failure + "'>";
        }else {
            header = "<response id='" + requestID + "' success='true'>";
        }
        String resetGameResponseHeader = String.format("<resetGameResponse gameId='%s'>", gameID);
        return header + resetGameResponseHeader;
    }

    /**
	 * Create standard response XML footer string for ListGames response.
	 * @return	Footer of the response.
	 */
    private String getFooter(){
        String boardResponseFooter = "</resetGameResponse>";
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
