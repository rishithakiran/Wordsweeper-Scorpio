package com.scorpio.server.protocol.response;
/**
 * The Failure Response holds the functionalities of the Failure responses,
 * necessary to send to the client. 
 * @author Josh
 */
public class FailureResponse {
    private final String reason;
    private final String requestID;
    
    /**
     * Constructor for Failure Response that handles reason and requestId.
     * @param reason	Reason for the failure.
     * @param requestID	Request ID.
     */
    public FailureResponse(String reason, String requestID){
        this.reason = reason;
        this.requestID = requestID;
    }

    /**
	 * The appropriate data is returned to the client.
	 * @return Header and Footer.
	 */
    public String toXML(){
        String header = String.format("<response id='" + requestID + "' success='false' reason='%s'>", this.reason);
        String footer = "</response>";

        return header + footer;
    }

}
