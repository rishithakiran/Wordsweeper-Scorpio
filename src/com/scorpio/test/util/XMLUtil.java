package com.scorpio.test.util;

import com.scorpio.serverbase.xml.Message;

import java.io.File;
import java.io.FileInputStream;

public class XMLUtil {
    /**
     * Build a Message object based on the given data
     * @param fileName The file name to read from
     * @return A constructed Message
     */
    public Message createMessageFromFile(String fileName){
        try {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String xmlString = new String(data, "UTF-8");
            xmlString = xmlString.replaceAll("\n[ ]*", "");

            Message msg = new Message(xmlString);
            return msg;
        }catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public XMLUtil(){
        Message.configure("wordsweeper.xsd");
    }

}
