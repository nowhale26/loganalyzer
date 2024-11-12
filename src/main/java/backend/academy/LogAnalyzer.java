package backend.academy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LogAnalyzer {
    private int requestCounter=0;
    private Map <String, Integer> mostResources;
    private Map <String, Integer> mostCodeResponses;
    private double averageSize=0;

    private static BufferedReader createReader(String path) throws IOException{
        if(path.startsWith("https://") || path.startsWith("http://")){
            URL url = URI.create(path).toURL();
            return new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            return new BufferedReader(new FileReader(path));
        }
    }

    public void analyzeLogs(){
        mostResources = new HashMap<>();
        mostCodeResponses = new HashMap<>();

    }
}
