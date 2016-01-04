/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iterativennclassifier;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
/**
 *
 * @author daniel
 */
public class IterativeNNClassifier {

    
    public static Map<List<String>,String> loadTrainFile(String file) throws IOException {
        Map<List<String>,String> dataToType = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line,":");
                String category = st.nextElement().toString();
                dataToType.put(convertSentenceToTokens(st.nextElement().toString()), category);
            }
        }
        System.out.print(dataToType.toString());
        return dataToType;
    }
    
    public static void train() {
        
    }
    
    public static String typeClassification(String query){
        List<String> tokens = convertSentenceToTokens(query);
        int i = runClassifier(tokens);
        return null;
    }
    
    public static int runClassifier(List<String> inputs) {
        return 1;
    }
    
    public static String cleanAndSplit(String str) {
        return str.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }
    
    public static List<String> convertSentenceToTokens(String str) {
        List<String> tokens = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(cleanAndSplit(str));
        while (st.hasMoreElements()) {
            tokens.add(st.nextElement().toString());
        }
        return tokens;
    }
    
}
