package backend.academy;

import lombok.Getter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class LogAnalyzer {
    private int requestCounter = 0;
    private Map<String, Integer> mostResources;
    private Map<String, Integer> mostCodeResponses;
    private long averageSize = 0;
    private int percentile95;
    private int maxSizeResponse = -1;
    private int minSizeResponse = -1;

    public void analyzeLogs(
        String pathPattern,
        LocalDateTime from,
        LocalDateTime to,
        String filterField,
        String filterValue
    ) {
        mostResources = new HashMap<>();
        mostCodeResponses = new HashMap<>();
        List<Integer> responseSizes = new ArrayList<>();

        LogFileReader logFileReader = new LogFileReader();
        logFileReader.getFiles(pathPattern).forEach(reader -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    Log log = parseLog(line);

                    if (log == null) {
                        continue;
                    }

                    if (!isWithinTimeRange(log.getDateTime(), from, to)) {
                        continue;
                    }

                    if (!matchesFilter(log, filterField, filterValue)) {
                        continue;
                    }

                    requestCounter++;
                    mostResources.put(log.getResource(), mostResources.getOrDefault(log.getResource(), 0) + 1);
                    mostCodeResponses.put(
                        String.valueOf(log.getResponseCode()),
                        mostCodeResponses.getOrDefault(String.valueOf(log.getResponseCode()), 0) + 1);
                    averageSize += log.getResponseSize();
                    maxSizeResponse = Math.max(log.getResponseSize(), maxSizeResponse);
                    if (minSizeResponse == -1) {
                        minSizeResponse = log.getResponseSize();
                    } else {
                        minSizeResponse = Math.min(log.getResponseSize(), minSizeResponse);
                    }
                    responseSizes.add(log.getResponseSize());
                }
                reader.close();
            } catch (IOException e) {
                throw new AnalyzerException(e);
            }
        });

        if (!responseSizes.isEmpty() && requestCounter > 0) {
            Collections.sort(responseSizes);
            int percentile95index = (int) Math.ceil(0.95 * responseSizes.size()) - 1;
            percentile95 = responseSizes.get(percentile95index);
            averageSize = averageSize / requestCounter;
        } else {
            percentile95 = 0;
            averageSize = 0;
        }
    }

    private static boolean isWithinTimeRange(LocalDateTime dateTime, LocalDateTime from, LocalDateTime to) {
        if (from != null && dateTime.isBefore(from)) {
            return false;
        }
        if (to != null && dateTime.isAfter(to)) {
            return false;
        }
        return true;
    }

    private static Log parseLog(String line) {
        String regex = "^(\\S+) \\S+ \\S+ \\[(.*?)\\] \"(\\S+) (\\S+) \\S+\" (\\d{3}) (\\d+|-) \"(.*?)\" \"(.*?)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if (!matcher.find()) {
            return null;
        }
        String ip = matcher.group(1);
        String dateTimeStr = matcher.group(2);
        String method = matcher.group(3);
        String resource = matcher.group(4);
        int responseCode = Integer.parseInt(matcher.group(5));
        String sizeStr = matcher.group(6);
        int responseSize = sizeStr.equals("-") ? 0 : Integer.parseInt(sizeStr);
        String agent = matcher.group(8);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStr, formatter);
        LocalDateTime dateTime = zonedDateTime.toLocalDateTime();

        return new Log(dateTime, responseCode, responseSize, resource, ip, method, agent);
    }

    private boolean matchesFilter(Log log, String filterField, String filterValue) {
        if (filterField == null || filterValue == null) {
            return true;
        }

        String fieldValue = null;
        switch (filterField.toLowerCase()) {
            case "agent":
                fieldValue = log.getAgent();
                break;
            case "method":
                fieldValue = log.getMethod();
                break;
            case "ip":
                fieldValue = log.getIp();
                break;
            case "resource":
                fieldValue = log.getResource();
                break;
            case "status":
            case "code":
                fieldValue = String.valueOf(log.getResponseCode());
                break;
            default:
                System.err.println("Неизвестное поле фильтрации: " + filterField);
                return false;
        }

        if (fieldValue == null) {
            return false;
        }

        String regexPattern = filterValue.replace("*", ".*");
        return fieldValue.matches(regexPattern);
    }
}
