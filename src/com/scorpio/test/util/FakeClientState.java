package com.scorpio.test.util;

import com.scorpio.server.core.ClientState;
import com.scorpio.xml.Message;

public class FakeClientState implements ClientState {
    private String id;
    private int msgsReceived = 0;
    private Message lastMessageReceived = null;
    public FakeClientState(String id){
        this.id = id;
    }

    @Override
    public boolean sendMessage(Message m) {
        this.lastMessageReceived = m;
        ++msgsReceived;
        return true;
    }

    public int getMsgsReceived(){
        return this.msgsReceived;
    }

    public Message getLastMessage(){
        return this.lastMessageReceived;
    }

    @Override
    public Object setData(Object newData) {
        return null;
    }

    @Override
    public Object getData() {
        return null;
    }

    public String id(){
        return this.id;
    }
}
