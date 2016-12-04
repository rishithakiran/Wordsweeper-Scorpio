package com.scorpio.server.exception;

public class WordSweeperException extends Exception {
    private final String reason;


    public WordSweeperException(String reason){
        this.reason = reason;
    }


    public String toString(){
        return reason;
    }
}
