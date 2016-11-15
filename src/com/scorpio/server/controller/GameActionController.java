package com.scorpio.server.controller;


import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.Game;
import com.scorpio.server.model.Player;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.xml.Message;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import org.w3c.dom.Node;

import java.util.List;
import java.util.stream.Collectors;

public class GameActionController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();
        String type = child.getLocalName();
        switch (type) {
            case "repositionBoardRequest": {
                String playerName = child.getAttributes().item(2).getNodeValue();
                String targetGame = child.getAttributes().item(1).getNodeValue();
                String rowChange = child.getAttributes().item(3).getNodeValue();
                String colChange = child.getAttributes().item(0).getNodeValue();

                try{
                    this.repositionBoard(playerName, targetGame, Integer.parseInt(colChange), Integer.parseInt(rowChange));
                }catch(WordSweeperException ex){
                    // Send a repositionBoardResponse with failure
                }

                // Notify everyone
                // Notify all players that the game has changed
                List<Player> players = GameManager.getInstance().findGameById(targetGame).getPlayers();
                // Filter out the player that just joined the game (they'll get
                // their own response)
                players = players.stream().filter((s) -> !(s.getName().equals(playerName)))
                        .collect(Collectors.toList());
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

                // Finally, send the response to the player that sent the original request
                BoardResponse br = new BoardResponse(playerName, targetGame, request.id(), false);
                return new Message(br.toXML());

            }
        }

        return null;
    }



    public void repositionBoard(String player, String gameID, int rowChange, int colChange) throws WordSweeperException {
        Game game = GameManager.getInstance().findGameById(gameID);

        // This may throw a WordSweeperException up the stack
        game.repositionPlayer(player, rowChange, colChange);
    }
}
