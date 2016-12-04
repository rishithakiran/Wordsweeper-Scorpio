package com.scorpio.server.exception;

/**
 * The Exception class for the Word Sweeper.
 * @author Josh
 */
public class WordSweeperException extends Exception {
    private final String reason;
    /**
     * Associates the exceptions thrown by other classes.
     * @param reason	The exception message.
     */
    public WordSweeperException(String reason){
        this.reason = reason;
    }

    /** Returns the Exception Message. */
    public String toString(){
        return reason;
    }
}
