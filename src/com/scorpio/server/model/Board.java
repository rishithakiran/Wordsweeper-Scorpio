package com.scorpio.server.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.scorpio.server.accessory.Coordinate;
import com.scorpio.test.util.XMLUtil;
import com.scorpio.xml.Message;

public class Board implements IModel {

	protected ArrayList<Tile> tiles;
	protected int size;

	public Board(int size) {
		this.tiles = new ArrayList<Tile>();
		this.size = size;
	}

	/**
	 * Get a square board that is a subset of this board
	 * @param start Location of the top-left corner of the sub board
	 * @param size Size of the sub board (it's a square)
	 * @return The sub board
	 */
	public Board getSubBoard(Coordinate start, int size){
		if(size + start.x > this.size || size + start.y > this.size){
			return null;
		}

		Board subBoard = new Board(size);
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
				Tile t = this.getTileAt(new Coordinate(start.x + x, start.y + y));
				// Make a fresh copy of this tile
				// This is inelegant, but I can't think of a prettier way to handle
				// the issue
				Tile tCopy = new Tile();
				tCopy.setContents(t.getContents());
				tCopy.setPoints(t.getPoints());
				tCopy.setMultiplier(t.getMultiplier());
				// Readdress the coordinate
				tCopy.setLocation(new Coordinate(x,y));
				subBoard.tiles.add(tCopy);
			}
		}
		return subBoard;
	}


	/**
	 * Given a coordinate, look at the board associated with this game an identify the
	 * tile there
	 * @param c The coordinate to use for lookup
	 * @return Tile at that location
	 */
	public Tile getTileAt(Coordinate c){
		List<Tile> l = this.tiles.stream().filter((t) -> t.getLocation().equals(c)).collect(Collectors.toList());
		if(l.size() != 1){
			return null;
		}
		return l.get(0);
	}

	/**
	 * @return Current height/width of the board (it's a square)
	 */
	public int getSize() {
		return size;
	}




	/**
	 * Serialize the board in preparation to be sent through a boardResponse message
	 * @return Board represented as a comma separated string
	 */
	public String toString(){
		String out = "";
		for(int x = 0; x < this.size; x++){
			for(int y = 0; y < this.size; y++){
				out += this.getTileAt(new Coordinate(x,y)).getContents() + ",";
			}
		}
		// Strip the trailing comma
		out = out.substring(0, out.length() - 1);
		return out;
	}

	/**
	 * The word formed by the player is validating by comparing to the file
	 * WordTable.sort - which contains the dictionary words
	 * @param Input Word
	 * @return True - valid word, False - invalid word
	 */
	
	public boolean isValidWord(Word w) {
		try {
				String input="C:/Users/Saranya/Documents/GitHub/Wordsweeper-Scorpio/resources/WordTable.sort";
			//	BufferedReader br = new BufferedReader(new FileReader(input));
		        String line; 
		        StringBuilder words = new StringBuilder(w.tiles.size());
		        
		        for ( int i = 0; i < w.tiles.size() ; i++)
		        {
		        	words.append(w.tiles.get(i).getContents().toLowerCase());
		        }
		       Scanner sc = new Scanner (new FileInputStream(input));
		       while (sc.hasNext()){
		    	   line = sc.next();
		    	   line  = line.toLowerCase();
		      	if(line.contentEquals(words))
					return true;
				else 
					continue;
			   }
		    //   br.close();
		     }
			 catch (IOException e) {
					e.printStackTrace();
				}	
				return false;
			}
		        
	/**
	 * Grows the board to match the given size. The board will always be
	 * a square. If this new size is smaller than the current board, there
	 * will be no effect
	 * @param newsize The new size to grow to
	 */
	public void grow(int newsize) {
		if(this.size >= newsize){
			return;
		}

		// Create the required new tiles
		for(int x = this.size; x < newsize; x++){
			for(int y = this.size; y < newsize; y++){
				Tile t = new Tile();
				t.setLocation(new Coordinate(x, y));
				this.tiles.add(t);
			}
		}

	}
	
	/**
	 * After the player formed the word, the tiles below the formed
	 * wordTile will float upwards and fill the gap created by 
	 * the formed word.
	 * @param c The location of the player
	 * @return updateBoard Updated board with new contents
	 **/
	public Board updateBoard(ArrayList<Tile> tiles, int size)
	{
		Board updated = new Board(size);
		Tile tilesMain = new Tile();
		
		int xCoord = 0 , yCoord = 0;
		
		for ( int iValue = 0; iValue < tiles.size() ; iValue++)
        {
			//tilesMain.setLocation(new Coordinate(xCoord,yCoord));
        	xCoord = tiles.get(iValue).getLocation().x;
        	yCoord = tiles.get(iValue).getLocation().y;
        	
        	//For loop to update the contents in the below tile to the above tile
        	for(int i = xCoord + 1 ; i < this.size ; i++)
        	{
        		for(int j = yCoord; j < this.size ; j++)
        		{
        			// Updating the from below tile
        			Tile t = new Tile();
        			t.setLocation(new Coordinate(i,j));
        			tilesMain.setContents(t.getContents());
        			tilesMain.setPoints(t.getPoints());
        			tilesMain.setMultiplier(t.getMultiplier());
        			updated.tiles.add(tilesMain);
           		}
        	}
        }
		return updated;
	}
}
