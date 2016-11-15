package com.scorpio.server.accessory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

    private Dictionary() throws IOException{
        String input = "WordTable.sort";
        BufferedReader br = new BufferedReader(new FileReader(input));
        String line;

        while ((line = br.readLine()) != null) {
            words.add(line.replaceAll("\\s+","") );
        }

        br.close();
    }

    public boolean isWord(String word){
        return this.words.contains(word);
    }

}
