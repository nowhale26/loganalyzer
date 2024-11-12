package backend.academy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgsParser {
    private LocalDateTime from;
    private LocalDateTime to;
    private String logfiles;
    private String format;
    private Map<String, String> arguments;

    private Map<String, String> parseArguments(String[] args){
        Map<String, String> argsMap = new HashMap<>();
        for(int i=0;i<args.length;i+=2){
            argsMap.put(args[i].replace("--",""),args[i+1]);
        }
        return argsMap;
    }

    private void initializeParametrs(){
        from = arguments.containsKey("from") ? LocalDateTime.parse(arguments.get("from")) : null;
        to = arguments.containsKey("to") ? LocalDateTime.parse(arguments.get("to")) : null;
        logfiles = arguments.get("path");
        format = arguments.getOrDefault("format","markdown");
    }
}