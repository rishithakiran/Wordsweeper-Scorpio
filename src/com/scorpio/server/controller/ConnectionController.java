package com.scorpio.server.controller;

import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.IShutdownHandler;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;
import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Responsible for directing inbound connections to the appropriate
 * controller
 */
public class ConnectionController implements IShutdownHandler {
    private final HashMap<String, Class> requestMapping;

    /**
     * Initialize this controller with the default mappings
     */
    public ConnectionController(){

        this.requestMapping = new HashMap<String, Class>();
        this.requestMapping.put("createGameRequest", GameAccessController.class);
        this.requestMapping.put("joinGameRequest", GameAccessController.class);
        this.requestMapping.put("repositionBoardRequest", GameActionController.class);
    }

    /**
     * Initialize this controller with a custom map of requests to
     * controllers
     * @param map Mapping of requests to controllers
     */
    public ConnectionController(HashMap<String, Class> map){
        this.requestMapping = map;
    }

    /**
     * Given a controller name, attempt to locate its class and
     * construct it with the given GameManager instance
     * @param controllerClass Class of the controller
     * @return Controller instance, null on error
     */
    private IProtocolHandler controllerFromClass(Class controllerClass) {
        try {
            Constructor controllerConstructor = controllerClass.getConstructor();
            IProtocolHandler controller = (IProtocolHandler) controllerConstructor.newInstance();
            return controller;
        }catch(Exception ex){
            // Log it out and return null. This doesn't warrant throwing
            // an exception up the stack, as this can be handled relatively
            // easily
            System.out.println(ex);
            return null;
        }
    }


    @Override
    public synchronized Message process(ClientState state, Message request) {
        // Safety first
        if(request == null){
            return null;
        }

        Node child = request.contents.getFirstChild();
        String type = child.getLocalName();
        Class controllerClass = this.requestMapping.get(type);
        if(controllerClass == null){
            // We don't have a mapping for this type of request
            return null;
        }

        IProtocolHandler cn = this.controllerFromClass(controllerClass);

        // If this returns null, there's a problem with one of our mappings
        return cn==null ? null : cn.process(state, request);
    }

    @Override
    public void logout(ClientState state) {

    }
}
