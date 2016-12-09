package com.scorpio.server.protocol.response;
/**
 * The Exit Game Response holds all functionalities with respect to the corresponding 
 * exit game requests, necessary to send to the client. 
 * @author Josh
 *
 */
public class ExitGameResponse {

    private final String requestID;
    private final String error;
    private final String gameId;
    
    /**
     * Constructor for ExitGame Response which handles gameId, requestId and error.
     * @param gameId		ID of the exit game.
     * @param requestID		Request ID.
     * @param error			Error Message if necessary.
     */
    public ExitGameResponse(String gameId, String requestID, String error){
        this.gameId = gameId;
        this.requestID = requestID;
        this.error = error;
    }
    
    /**
     * Constructor for ExitGame Response which handles gameId and requestId.
     * @param gameId		ID of the exit game.
     * @param requestID		Request ID.
     */
    public ExitGameResponse(String gameId, String requestID){
        this.gameId = gameId;
        this.requestID = requestID;
        this.error = null;
    }

    /**
	 * Create standard response XML header string for successful response or failure response.
	 * And create a exit game response XML header with gameId.
	 * @return	Header of the response.
	 */
    private String getHeader(){

        String header;
        if(this.error != null){
            header = "<response id='" + requestID + "' success='false' reason='" + this.error +"'>";
        }else {
            header = "<response id='" + requestID + "' success='true'>";
        }
        String boardResponseHeader = String.format("<exitGameResponse gameId='%s'>", gameId);

        return header + boardResponseHeader;
    }

    /**
	 * Create standard response XML footer string for Exit game response.
	 * @return	Footer of the response.
	 */
    private String getFooter(){
        String boardResponseFooter = "</exitGameResponse>";
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
