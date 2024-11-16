package backend.academy;

import lombok.Getter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private double averageSize = 0;
    private int percentile95;

    private static BufferedReader createReader(String path) throws IOException {
        if (path.startsWith("https://") || path.startsWith("http://")) {
            URL url = URI.create(path).toURL();
            return new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            return new BufferedReader(new FileReader(path));
        }
    }

    public void analyzeLogs(String path, LocalDateTime from, LocalDateTime to) {
        mostResources = new HashMap<>();
        mostCodeResponses = new HashMap<>();
        List<Integer> responseSizes = new ArrayList<>();

        if (!(path.startsWith("http://") || path.startsWith("https://"))) {
            Path filePath = Paths.get(path);
            if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                System.err.println("Файл не найден: " + path);
                return;
            }
        }
        try (BufferedReader reader = createReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Log log = parseLog(line);
                if (isWithinTimeRange(log.getDateTime(), from, to)) {
                    requestCounter++;
                    mostResources.put(log.getResource(), mostResources.getOrDefault(log.getResource(), 0) + 1);
                    mostCodeResponses.put(
                        String.valueOf(log.getResponseCode()),
                        mostCodeResponses.getOrDefault(log.getResponseCode(), 0) + 1);
                    averageSize += log.getResponseSize();
                    responseSizes.add(log.getResponseSize());
                }
            }
            Collections.sort(responseSizes);
            int percentile95index = (int) Math.ceil(0.95 * responseSizes.size()) - 1;
            percentile95 = responseSizes.get(percentile95index);
            averageSize = averageSize / requestCounter;

        } catch (IOException e) {
            throw new RuntimeException(e);
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
        String regex = "^(\\S+) \\S+ \\S+ \\[(.*?)\\] \"(\\S+) (\\S+) \\S+\" (\\d{3}) (\\d+|-)";
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
        int responseSize = Integer.parseInt(matcher.group(6));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStr, formatter);
        LocalDateTime dateTime = zonedDateTime.toLocalDateTime();

        return new Log(dateTime, responseCode, responseSize, resource, ip, method);
    }
}
