package com.scorpio.test.unit.server.accessory;
import com.scorpio.server.accessory.Dictionary;
import org.junit.Test;


public class DictionaryTest {
    @Test
    public void functionality_IsWord(){
        Dictionary d = Dictionary.getInstance();

        assert(d.isWord("something"));
        assert(!d.isWord("notaword"));
    }

}
