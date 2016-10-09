package com.scorpio.test.util;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.xml.Message;

/**
 * This is a protocol handler that has the ability to detect
 * when it has been tripped.
 */
public class TestHandler implements IProtocolHandler {
    public static Trigger t;

    public TestHandler(GameManager gm){}

    @Override
    public Message process(ClientState state, Message request) {
        t.trip();
        return null;
    }
}
