package com.scorpio.server.protocol.response;


public class FailureResponse {
    private final String reason;
    private final String requestID;
    public FailureResponse(String reason, String requestID){
        this.reason = reason;
        this.requestID = requestID;
    }

    public String toXML(){

        String header = String.format("<response id='" + requestID + "' success='false' reason='%s'>", this.reason);
        String footer = "</response>";


        return header + footer;
    }

}
