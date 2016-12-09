package com.scorpio.test.unit.server.accessory;
import com.scorpio.server.accessory.Dictionary;
import org.junit.Test;
/**
 * Test case for Validating the Dictionary word.
 * @author Saranya
 *
 */

public class DictionaryTest {
	/**Test for validating a dictionary word*/
    @Test
    public void functionality_IsWord(){
        Dictionary d = Dictionary.getInstance();

        assert(d.isWord("something"));
        assert(!d.isWord("notaword"));
    }

}
