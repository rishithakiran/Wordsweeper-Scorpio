package com.scorpio.server.protocol.response;

public class ExitGameResponse {

    private final String requestID;
    private final String error;
    private final String gameId;

    public ExitGameResponse(String gameId, String requestID, String error){
        this.gameId = gameId;
        this.requestID = requestID;
        this.error = error;
    }

    public ExitGameResponse(String gameId, String requestID){
        this.gameId = gameId;
        this.requestID = requestID;
        this.error = null;
    }

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


    private String getFooter(){
        String boardResponseFooter = "</exitGameResponse>";
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
