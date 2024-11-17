package backend.academy.textreports;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class TextReport {
    protected final int requestCounter;
    protected final Map<String, Integer> mostResources;
    protected final Map<String, Integer> mostCodeResponses;
    protected final long averageSize;
    protected final int percentile95;
    protected final String path;
    protected final LocalDateTime from;
    protected final LocalDateTime to;
    protected final int maxSizeResponse;
    protected final int minSizeResponse;

    public TextReport(
        int requestCounter,
        Map<String, Integer> mostResources,
        Map<String, Integer> mostCodeResponses,
        long averageSize,
        int percentile95,
        String path,
        LocalDateTime from,
        LocalDateTime to,
        int maxSizeResponse,
        int minSizeResponse
    ) {
        this.requestCounter = requestCounter;
        this.mostResources = new HashMap<>(mostResources);
        this.mostCodeResponses = new HashMap<>(mostCodeResponses);
        this.averageSize = averageSize;
        this.percentile95 = percentile95;
        this.path = path;
        this.from = from;
        this.to = to;
        this.maxSizeResponse = maxSizeResponse;
        this.minSizeResponse = minSizeResponse;
    }

    public void generateReport() {
    }

    protected static void writeReportToFile(String reportContent, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(reportContent);
            System.out.println("Отчет успешно сохранен в файл: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при записи отчета в файл: " + fileName);
            e.printStackTrace();
        }
    }

    protected static String extractFileName(String path) {
        int lastSeparatorIndex = path.lastIndexOf('/');
        return path.substring(lastSeparatorIndex + 1);
    }

}
