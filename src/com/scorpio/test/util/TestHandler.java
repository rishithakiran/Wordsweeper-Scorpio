package com.scorpio.test.util;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.serverbase.xml.Message;

/**
 * This is a protocol handler that has the ability to detect
 * when it has been tripped.
 */
public class TestHandler implements IProtocolHandler {
    public static Trigger t;

    @Override
    public Message process(ClientState state, Message request) {
        t.trip();
        return null;
    }
}
