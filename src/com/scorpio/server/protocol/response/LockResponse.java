package com.scorpio.server.protocol.response;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Game;

public class LockResponse {
		//private final String reason;
	    private final String requestID;
	    private final String gameID;
	    public LockResponse(String gameID, String requestID){
	       // this.reason = reason;
	        this.requestID = requestID;
	        this.gameID=gameID;
	    }

	    private String getHeader(){
	        Game g = GameManager.getInstance().findGameById(gameID);
	        String managingUser = g.getManagingPlayer().getName();
	        String header = "<response id='" + requestID + "' success='true'>";
	        String lockResponseHeader = String.format("<lockResponse gameId='%s' managingUser='%s'", gameID, managingUser);

	        return header + lockResponseHeader;
	    }
	    
	    private String getFooter(){
	        String lockResponseFooter = "</lockResponse>";
	        String footer = "</response>";

	        return lockResponseFooter + footer;
	    }
	    
	    public String toXML(){
	        String complete = this.getHeader();
	        String s=this.getFooter();
	        return complete + s;
	    }
}
	        
	    
	    
	    
	   
