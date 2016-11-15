package com.scorpio.server.protocol.response;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ResetGameResponse {
    private final String gameID;
    private final String requestID;
    private final String failure;
    public ResetGameResponse(String gameID, String requestID, String failure){
        this.gameID = gameID;
        this.requestID = requestID;
        this.failure = failure;
    }

    public ResetGameResponse(String gameID, String requestID){
        this.gameID = gameID;
        this.requestID = requestID;
        this.failure = null;
    }

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


    private String getFooter(){
        String boardResponseFooter = "</resetGameResponse>";
        String footer = "</response>";

        return boardResponseFooter + footer;
    }

    public String toXML(){
        String complete = this.getHeader();
        complete += this.getFooter();
        // We will return the appropriate data to the client
        return complete;
    }




}
