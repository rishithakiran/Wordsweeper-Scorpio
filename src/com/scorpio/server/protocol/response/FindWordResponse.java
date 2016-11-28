package com.scorpio.server.protocol.response;

import com.scorpio.server.core.GameManager;
import com.scorpio.server.model.Board;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;

import java.util.ArrayList;
import java.util.List;

public class FindWordResponse {
    private final String playerID;
    private final String gameID;
    private final String requestID;
    private long score;
    private final String error;
    public FindWordResponse(String playerID, String gameID, String requestID, int score){
        this.playerID = playerID;
        this.gameID = gameID;
        this.requestID = requestID;
        this.score = score;
        this.error = null;
    }

    public FindWordResponse(String playerID, String gameID, String requestID, String error){
        this.playerID = playerID;
        this.gameID = gameID;
        this.requestID = requestID;
        this.score = 0;
        this.error = error;
    }

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


    private String getFooter(){
        String boardResponseFooter = "</findWordResponse>";
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
