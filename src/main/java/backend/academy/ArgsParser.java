package backend.academy;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ArgsParser {
    private LocalDateTime from;
    private LocalDateTime to;
    private String path;
    private String format;
    private String filterField;
    private String filterValue;
    private Map<String, String> arguments;

    @SuppressFBWarnings("PSC_PRESIZE_COLLECTIONS")
    private Map<String, String> parseArguments(String[] args) {
        Map<String, String> argsMap = new HashMap<>();

        for (int i = 0; i < args.length; i += 2) {
            argsMap.put(args[i].replace("--", ""), args[i + 1]);
        }
        return argsMap;
    }

    @SuppressFBWarnings("MUI_CONTAINSKEY_BEFORE_GET")
    public void initializeArguments(String[] args) {
        arguments = new HashMap<>(parseArguments(args));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String fromValue = arguments.get(Constants.FROM);
        from = (fromValue != null) ? LocalDateTime.parse(fromValue, formatter) : null;
        String toValue = arguments.get("to");
        to = (toValue != null) ? LocalDateTime.parse(toValue, formatter) : null;
        if (arguments.containsKey(Constants.PATH)) {
            path = arguments.get(Constants.PATH);
        } else {
            System.err.println("Не указан обязательный параметр --path");
        }
        format = arguments.getOrDefault("format", "markdown");
        filterField = arguments.get("filter-field");
        filterValue = arguments.get("filter-value");
    }
}
