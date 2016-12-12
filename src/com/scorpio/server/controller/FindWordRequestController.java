package com.scorpio.server.controller;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.server.accessory.Session;
import com.scorpio.server.core.ClientState;
import com.scorpio.server.core.GameManager;
import com.scorpio.server.exception.WordSweeperException;
import com.scorpio.server.model.*;
import com.scorpio.server.protocol.IProtocolHandler;
import com.scorpio.server.protocol.response.FailureResponse;
import com.scorpio.server.protocol.response.FindWordResponse;
import com.scorpio.xml.Message;
import org.w3c.dom.Node;

import java.util.ArrayList;
/**
 * Module that handles Find word request.
 * @author Saranya
 *
 */
public class FindWordRequestController implements IProtocolHandler {
    @Override
    public Message process(ClientState state, Message request) {
        Node child = request.contents.getFirstChild();
        Node nextChild = child.getFirstChild();

        // Establish which game and what player
        String playerId = child.getAttributes().getNamedItem("name").getNodeValue();
        String gameId = child.getAttributes().getNamedItem("gameId").getNodeValue();

        // Ensure this person is actually who they say they are
        Session session = (Session) state.getData();
        if(session == null){
            FindWordResponse fwr = new FindWordResponse(
                    playerId,gameId,request.id(),
                    "Who are you?"
            );
            return new Message(fwr.toXML());
        }

        // Verify session data here
        if(!session.getGame().getId().equals(gameId) || !session.getPlayer().getName().equals(playerId)){
            FindWordResponse fwr = new FindWordResponse(
                    playerId,gameId,request.id(),
                    "You're lying to me"
            );
            return new Message(fwr.toXML());
        }

        // Extract the cells of this request that form a word
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        while(nextChild != null){
            String s = nextChild.getAttributes().getNamedItem("letter").getNodeValue();
            String loc = nextChild.getAttributes().getNamedItem("position").getNodeValue();
            // Parse the CSV loc into an row and col value
            String[] xy = loc.split(",");
            Tile t = new Tile(s, new Coordinate(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
            tiles.add(t);
            nextChild = nextChild.getNextSibling();
        }
        Word w = new Word(tiles);


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

    /**
     * The location of the tile related to the input word from the players board 
     * is retrieved and associated with the global board where the word is validated
     * and valid word is removed and board is updated. 
     * @param 	w	The input word to be validated.	
     * @param 	playerName	Name of the player requesting for validating the word.
     * @param 	game	The current game of the player.
     * @throws 	WordSweeperException	Throws appropriate WordSweeper exception.
     */
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
            Coordinate newLoc = new Coordinate(current.col - (player0Point.col - 1), current.row - (player0Point.row - 1));
            nT.setLocation(newLoc);
            nT.setContents(t.getContents());
            nT.setBonus(t.isBonus());

            // Figure out the multiplier on this tile by counting how many players
            // have this tile on their board
            int sharedBy = 0;
            for(Player p : g.getPlayers()){
                if(p.hasTile(t)){
                    ++sharedBy;
                }
            }

            nT.setSharedBy(sharedBy);
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
}
