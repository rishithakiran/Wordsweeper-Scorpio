package com.scorpio.server.accessory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Responsible for validating the input word.
 * 
 * @author Saranya, Josh
 */
public class Dictionary {
    private static Dictionary instance = null;
    private ArrayList<String> words = new ArrayList<String>();


    public static Dictionary getInstance(){
        if(instance == null){
            try {
                instance = new Dictionary();
            }catch (IOException e){
                System.out.println(e);
            }
        }
        return instance;
    }

    /**
     * Reads the words in the file "WordTable.sort".
     * The words from the file is stored to ArrayList of String type.
     * @throws IOException
     */
    private Dictionary() throws IOException{
        String input = "WordTable.sort";
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line;

        while ((line = br.readLine()) != null) {
            words.add(line.replaceAll("\\s+","") );
        }

        br.close();
    }
  
    /**
     * Validates the input word.
     * @param 	word	The input word which is to be validated.
     * @return	true:	If the input word is valid Dictionary word.
     * <p>		false:	If the input word is invalid Dictionary word.</p>
     */
   public boolean isWord(String word){
        return this.words.contains(word);
    }

}
