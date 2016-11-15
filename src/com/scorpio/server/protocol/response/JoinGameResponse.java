package com.scorpio.server.protocol.response;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

import java.util.ArrayList;
import java.util.List;

public class JoinGameResponse {

    private final String requestID;
    private final String error;
    private final String gameId;

    public JoinGameResponse(String gameId, String requestID, String error){
        this.gameId = gameId;
        this.requestID = requestID;
        this.error = error;
    }


    private String getHeader(){

        String header;
        if(this.error != null){
            header = "<response id='" + requestID + "' success='false' reason='" + this.error +"'>";
        }else {
            header = "<response id='" + requestID + "' success='true'>";
        }
        String boardResponseHeader = String.format("<joinGameResponse gameId='%s'>", gameId);

        return header + boardResponseHeader;
    }


    private String getFooter(){
        String boardResponseFooter = "</joinGameResponse>";
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
