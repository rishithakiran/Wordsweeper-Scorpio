package com.scorpio.server.controller;


import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

import java.util.List;
import java.util.stream.Collectors;

public class GameManagementController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();
        String type = child.getLocalName();
        switch (type) {
            case "resetGameRequest": {
                String targetGame = child.getAttributes().item(1).getNodeValue();

                try{
                    throw new WordSweeperException("...");
                }catch(WordSweeperException ex){

                }

                // Notify everyone
                // Notify all players that the game has changed
                List<Player> players = GameManager.getInstance().findGameById(targetGame).getPlayers();

                for (Player p : players) {
                    // If we've lost the client state, pass
                    if (p.getClientState() == null) {
                        continue;
                    }

                    String requestID = p.getClientState().id();
                    BoardResponse br = new BoardResponse(p.getName(), targetGame, requestID, false);
                    Message brm = new Message(br.toXML());
                    p.getClientState().sendMessage(brm);
                }

                // Finally, send a resetGameResponse to the initiator
                // TODO
                return null;

            }
        }

        return null;
    }


    public void resetGame(String gameId){


    }
}
