package com.scorpio.server.controller;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.*;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.BoardResponse;
import com.scorpio.server.protocol.response.FindWordResponse;
import com.scorpio.xml.Message;
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

                GameManager.getInstance().findGameById(targetGame).notifyPlayers();

                // We can return null here because notifyPlayers will notify everyone; including
                // the person who jsut joined!
                return null;

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
                    Tile t = new Tile(s, new Coordinate(Integer.parseInt(xy[0]) - 1, Integer.parseInt(xy[1]) - 1));
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
                    FindWordResponse fwr = new FindWordResponse(
                            playerId,gameId,request.id(),
                            ex.toString()
                    );
                    return new Message(fwr.toXML());
                }


                // Notify all players of chnage to board state
                GameManager.getInstance().findGameById(gameId).notifyPlayers();


                // Send findWordResponse
                FindWordResponse fwr = new FindWordResponse(
                        playerId,gameId,request.id(),
                        GameManager.getInstance().findGameById(gameId).getPlayer(playerId).getScore()
                );
                return new Message(fwr.toXML());
            }
        }

        return null;
    }


    public void findWord(Word w, String playerName, String game) throws WordSweeperException{
        Game g = GameManager.getInstance().findGameById(game);
        if(g == null){
            throw new WordSweeperException("Game does not exist");
        }

        Player player = g.getPlayer(playerName);
        if(player == null){
            throw new WordSweeperException("Who are you?!");
        }

        Board pb = g.getPlayerBoard(playerName);
        if(pb == null){
            throw new WordSweeperException("Internal error");
        }

        // We also need to normalize w based on the player's current location, as w uses
        // absolute coordinates, while pb uses a relative system
        ArrayList<Tile> relativeTiles = new ArrayList<>();
        for(Tile t : w.tiles){
            Tile nT = new Tile();
            Coordinate current = t.getLocation();
            Coordinate player0Point = g.getPlayer(playerName).getLocation();
            Coordinate newLoc = new Coordinate(current.x - player0Point.x, current.y - player0Point.y);
            nT.setLocation(newLoc);
            nT.setContents(t.getContents());
            nT.setMultiplier(t.getMultiplier());
            nT.setSharedBy(t.getSharedBy());
            relativeTiles.add(nT);
        }
        Word relativeW = new Word(relativeTiles);

        // Affirm that word exists in the player's board, and then remove it from the global board
        if(pb.hasWord(relativeW)){
            // Calculate the point value of word submitted and grant the
            // points to the player
            int score = relativeW.computeScore();
            player.setScore(player.getScore() + score);

            g.getBoard().removeWord(w);
        }else{
            throw new WordSweeperException("Word is not available in your board!");
        }
    }

    public void repositionBoard(String player, String gameID, int rowChange, int colChange) throws WordSweeperException {
        Game game = GameManager.getInstance().findGameById(gameID);

        // This may throw a WordSweeperException up the stack
        game.repositionPlayer(player, rowChange, colChange);
    }
}
