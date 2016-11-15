package com.scorpio.server.protocol.response;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Game;

public class LockGameResponse {
		private final String error;
	    private final String requestID;
	    private final String gameID;

	    public LockGameResponse(String gameID, String requestID, String error){
	        this.error = error;
	        this.requestID = requestID;
	        this.gameID=gameID;
	    }

        public LockGameResponse(String gameID, String requestID){
            this.error = null;
            this.requestID = requestID;
            this.gameID=gameID;
        }

	    private String getHeader(){
	        Game g = GameManager.getInstance().findGameById(gameID);
	        String managingUser = g.getManagingPlayer().getName();
            String header;
            if(this.error == null){
                header = "<response id='" + requestID + "' success='false' reason='" + error + "'>";
            }else{
                header = "<response id='" + requestID + "' success='true'>";
            }
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
	        
	    
	    
	    
	   
