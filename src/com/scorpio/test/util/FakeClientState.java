package com.scorpio.test.util;

import com.scorpio.server.core.ClientState;
import com.scorpio.xml.Message;

public class FakeClientState implements ClientState {
    private String id;
    public FakeClientState(String id){
        this.id = id;
    }

    @Override
    public boolean sendMessage(Message m) {
        return false;
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
