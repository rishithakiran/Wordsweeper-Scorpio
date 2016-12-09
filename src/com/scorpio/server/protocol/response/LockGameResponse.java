package com.scorpio.server.protocol.response;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Game;
/**
 * The Lock game Response holds all functionalities with respect to the corresponding 
 * Lock game requests, necessary to send to the client. 
 * @author Rishitha
 *
 */
public class LockGameResponse {
		private final String error;
	    private final String requestID;
	    private final String gameID;

	    /**
	     * Constructor for Lock game response that handles gameID, requestID and error.
	     * @param gameID	ID of the game to be locked.
	     * @param requestID	Request ID.
	     * @param error		Error message if unable to lock.
	     */
	    public LockGameResponse(String gameID, String requestID, String error){
	        this.error = error;
	        this.requestID = requestID;
	        this.gameID = gameID;
	    }

	    /**
	     * Constructor for Lock game response that handles gameID and requestID.
	     * @param gameID	ID of the game to be locked.
	     * @param requestID	Request ID.
	     */
        public LockGameResponse(String gameID, String requestID){
            this.error = null;
            this.requestID = requestID;
            this.gameID = gameID;
        }

        /**
    	 * Create standard response XML header string for successful response or failure response.
    	 * And create a lock response with gameId.
    	 * @return	Header of the response.
    	 */
	    private String getHeader(){
	        Game g = GameManager.getInstance().findGameById(gameID);
	        String managingUser = g.getManagingPlayer().getName();
            String header;
            if(this.error != null){
                header = "<response id='" + requestID + "' success='false' reason='" + error + "'>";
            }else{
                header = "<response id='" + requestID + "' success='true'>";
            }
	        String lockResponseHeader = String.format("<lockGameResponse gameId='%s'>", gameID, managingUser);

	        return header + lockResponseHeader;
	    }
	    

		/**
		 * Create standard response XML footer string for ListGames response.
		 * @return	Footer of the response.
		 */
	    private String getFooter(){
	        String lockResponseFooter = "</lockGameResponse>";
	        String footer = "</response>";

	        return lockResponseFooter + footer;
	    }
	    
	    /**
		 * The appropriate data is returned to the client.
		 * @return Header and Footer.
		 */
	    public String toXML(){
	        String complete = this.getHeader();
	        String s = this.getFooter();
	        return complete + s;
	    }
}
	        
	    
	    
	    
	   
