package com.scorpio.test.server.controller;

import com.scorpio.server.controller.ConnectionController;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.model.GameManager;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.test.TestHandler;
import com.scorpio.test.Trigger;
import com.scorpio.xml.Message;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;



public class ConnectionControllerTest {



    /**
     * Build a Message object based on the given data
     * @param fileName The file name to read from
     * @return A constructed Message
     */
    private Message createMessageFromFile(String fileName){
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String xmlString = new String(data, "UTF-8");


            Message msg = new Message(xmlString);
            return msg;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    @BeforeClass
    public static void configureXML(){
        Message.configure("wordsweeper.xsd");
    }

    @Test
    /**
     * Ensure that with a given mapping, our connection controller routes us
     * to the correct place
     */
    public void routing_Basic(){
        HashMap<String, Class> mapping = new HashMap<String, Class>();
        mapping.put("createGameRequest", TestHandler.class);

        ConnectionController router = new ConnectionController(GameManager.getInstance(), mapping);
        Message msg = createMessageFromFile("testxml/createGameRequest.xml");
        TestHandler.t = new Trigger();

        router.process(null, msg);

        assert(TestHandler.t.isTripped());

    }

    @Test
    /**
     * Ensure that if we somehow map to a controller that doesn't exist, the
     * server doesn't crash and burn
     */
    public void resiliency_BadClass(){
        HashMap<String, Class> mapping = new HashMap<String, Class>();

        ConnectionController router = new ConnectionController(GameManager.getInstance(), mapping);
        Message msg = createMessageFromFile("testxml/createGameRequest.xml");

        assert( router.process(null, msg) == null );
    }

    @Test
    /**
     * Ensure that mapping to a malformed class is handled gracefully
     */
    public void resiliency_MalformedClass(){
        HashMap<String, Class> mapping = new HashMap<String, Class>();
        class BadClass implements IProtocolHandler{
            // Constructor not implemented, which is bad

            @Override
            public Message process(ClientState state, Message request) {
                return null;
            }
        }

        mapping.put("createGameRequest", BadClass.class);

        ConnectionController router = new ConnectionController(GameManager.getInstance(), mapping);
        Message msg = createMessageFromFile("testxml/createGameRequest.xml");
        TestHandler.t = new Trigger();

        assert(null == router.process(null, msg));
    }

    @Test
    /**
     * Validate that if we're passed an empty message, nothing bad happens
     */
    public void resiliency_BadMessage(){
        ConnectionController router = new ConnectionController(GameManager.getInstance());
        assert( router.process(null, null) == null );
    }

}