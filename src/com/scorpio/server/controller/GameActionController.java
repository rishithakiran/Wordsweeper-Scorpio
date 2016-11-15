package com.scorpio.server.controller;


import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.*;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.xml.Message;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
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
                    BoardResponse br = new BoardResponse(playerName, targetGame, request.id(), false, ex.toString());
                    return new Message(br.toXML());
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
            case "findWordRequest": {
                // Extract the cells of this request that form a word
                ArrayList<Tile> tiles = new ArrayList<Tile>();
                Node nextChild = child.getFirstChild();
                while(nextChild != null){
                    String s = nextChild.getAttributes().item(0).getNodeValue();
                    String loc = nextChild.getAttributes().item(1).getNodeValue();
                    // Parse the CSV loc into an x and y value
                    String[] xy = loc.split(",");
                    Tile t = new Tile(s, new Coordinate(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
                    tiles.add(t);
                    nextChild = nextChild.getNextSibling();
                }
                Word w = new Word(tiles);

                // Establish which game and what player
                String playerId = child.getAttributes().getNamedItem("name").getNodeValue();
                String gameId = child.getAttributes().getNamedItem("gameId").getNodeValue();
                try {
                    this.findWord(w, playerId, gameId);
                }catch(WordSweeperException ex){
                    /// zzz
                }
            }
        }

        return null;
    }


    public void findWord(Word w, String player, String game) throws WordSweeperException{
        Game g = GameManager.getInstance().findGameById(game);
        Board pb = g.getPlayerBoard(player);

        // We also need to normalize w based on the player's current location, as w uses
        // absolute coordinates, while pb uses a relative system
        ArrayList<Tile> relativeTiles = new ArrayList<>();
        for(Tile t : w.tiles){
            Tile nT = new Tile();
            Coordinate current = t.getLocation();
            Coordinate player0Point = g.getPlayer(player).getLocation();
            Coordinate newLoc = new Coordinate(current.x - player0Point.x, current.y - player0Point.y);
            nT.setLocation(newLoc);
            nT.setContents(t.getContents());
            relativeTiles.add(nT);
        }
        Word relativeW = new Word(relativeTiles);

        if(pb.hasWord(relativeW)){

        }

    }

    public void repositionBoard(String player, String gameID, int rowChange, int colChange) throws WordSweeperException {
        Game game = GameManager.getInstance().findGameById(gameID);

        // This may throw a WordSweeperException up the stack
        game.repositionPlayer(player, rowChange, colChange);
    }
}
