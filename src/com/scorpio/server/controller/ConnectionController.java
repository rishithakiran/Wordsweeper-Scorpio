package com.scorpio.server.controller;

import com.scorpio.server.accessory.Session;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.model.Game;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.IShutdownHandler;
import com.scorpio.serverbase.xml.Message;
import org.w3c.dom.Node;
import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Responsible for directing inbound connections to the appropriate
 * controller
 * @author Josh
 * @author Apoorva
 * @author Rishitha
 * @author Saranya
 */
public class ConnectionController implements IShutdownHandler {
    private final HashMap<String, Class> requestMapping;

    /**
     * Initialize this controller with the default mappings
     */
    public ConnectionController(){
        this.requestMapping = new HashMap<String, Class>();
        this.requestMapping.put("createGameRequest", CreateGameRequestController.class);
        this.requestMapping.put("joinGameRequest", JoinGameRequestController.class);
        this.requestMapping.put("exitGameRequest", ExitGameRequestController.class);
        this.requestMapping.put("lockGameRequest", LockGameRequestController.class);
        this.requestMapping.put("repositionBoardRequest", RepositionBoardRequestController.class);
        this.requestMapping.put("resetGameRequest", ResetGameRequestController.class);
        this.requestMapping.put("listGamesRequest", ListGamesRequestController.class);
        this.requestMapping.put("findWordRequest", FindWordRequestController.class);
        this.requestMapping.put("showGameStateRequest", ShowGameStateRequestController.class);

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

    /**
     * Called when a client unexpectedly closes the connection. In this case, the server needs to
     * figure out if they had any active sessions, and if they do, close them out
     * @param state ClientState of the request
     */
    @Override
    public void logout(ClientState state) {
        // Using the stored session, ensure that this player is removed
        Session s = (Session) state.getData();

        if(s == null){
            // Not my problem
            return;
        }
        Game g = s.getGame();
        if(g.getPlayer(s.getPlayer().getName()) != null){
            // The player did not tell us they were exiting
            ExitGameRequestController egr = new ExitGameRequestController();
            try {
                egr.exitGame(s.getPlayer().getName(), g.getId());
                g.notifyPlayers();
            }catch(Exception e){
                // uhh...
                // It's fine
                // Probably
            }
        }
    }
}