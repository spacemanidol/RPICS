/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import iterativennclassifier.IterativeNNClassifier;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 *
 * @author daniel
 */
public class INNTests {
    
    @Before
    public void setUp() {
        //Load NN
        //Train NN
        //Parse 
        //Test type output
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testParser() {
        String sentence = "What is the weather like";
        List<String> result = IterativeNNClassifier.convertSentenceToTokens(sentence);
        assertEquals("[What, is, the, weather, like]",result.toString());
    }
    
    @Test
    public void testRemoveCharacters() {
        String sentence = "What is the, weather like?";
        String result = IterativeNNClassifier.cleanAndSplit(sentence);
        assertEquals("what is the weather like",result);
    }
   
    @Test
    public void testDataLoader() {
        try {
            IterativeNNClassifier.loadTrainFile("traindata.txt");
        } catch (IOException E) {
            fail("File not found, Training Data Unable to load");
        }
    }
}
