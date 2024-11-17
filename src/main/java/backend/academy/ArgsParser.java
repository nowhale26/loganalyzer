package backend.academy;

import lombok.Getter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ArgsParser {
    private LocalDateTime from;
    private LocalDateTime to;
    private String path;
    private String format;
    private String filterField;
    private String filterValue;
    private Map<String, String> arguments;

    private Map<String, String> parseArguments(String[] args) {
        Map<String, String> argsMap = new HashMap<>();

        for (int i = 0; i < args.length; i += 2) {
            argsMap.put(args[i].replace("--", ""), args[i + 1]);
        }
        return argsMap;
    }

    public void initializeArguments(String[] args) {
        arguments = new HashMap<>(parseArguments(args));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        from = arguments.containsKey("from") ? LocalDateTime.parse(arguments.get("from"), formatter) : null;
        to = arguments.containsKey("to") ? LocalDateTime.parse(arguments.get("to"),formatter) : null;
        if (arguments.containsKey("path")) {
            path = arguments.get("path");
        } else {
            System.err.println("Не указан обязательный параметр --path");
        }
        format = arguments.getOrDefault("format", "markdown");
        filterField = arguments.get("filter-field");
        filterValue = arguments.get("filter-value");
    }
}
