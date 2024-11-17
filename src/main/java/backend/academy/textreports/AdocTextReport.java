package backend.academy.textreports;

import java.time.LocalDateTime;
import java.util.Map;

public class AdocTextReport extends TextReport {

    public AdocTextReport(
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
        super(requestCounter, mostResources, mostCodeResponses, averageSize, percentile95, path, from, to,
            maxSizeResponse, minSizeResponse);
    }

    @Override
    public void generateReport() {
        StringBuilder report = new StringBuilder();
        String name = extractFileName(path);
        report.append("= Общая информация\n\n");
        report.append("== Имя файла\n\n").append(name).append("\n\n");
        report.append("== Начальная дата\n\n").append(from == null ? "-" : from.toString()).append("\n\n");
        report.append("== Конечная дата\n\n").append(to == null ? "-" : to.toString()).append("\n\n");
        report.append("== Общее количество запросов\n\n").append(requestCounter).append("\n\n");
        report.append("== Средний размер ответа\n\n").append(averageSize).append("\n\n");
        report.append("== 95p размер ответа\n\n").append(percentile95).append("\n\n");
        report.append("== Наибольший размер ответа\n\n").append(maxSizeResponse).append("\n\n");
        report.append("== Наименьший размер ответа\n\n").append(minSizeResponse).append("\n\n");

        report.append("== Наиболее часто запрашиваемые ресурсы\n\n");
        report.append("[cols=\"2,1\", options=\"header\"]\n|===\n| Ресурс | Количество запросов\n");
        mostResources.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(3)
            .forEach(entry -> report.append("| ").append(entry.getKey())
                .append("\n| ").append(entry.getValue()).append("\n"));
        report.append("|===\n");

        report.append("\n== Наиболее часто встречающиеся коды ответа\n\n");
        report.append("[cols=\"1,1\", options=\"header\"]\n|===\n| Код ответа | Количество\n");
        mostCodeResponses.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(3)
            .forEach(entry -> report.append("| ").append(entry.getKey())
                .append("\n| ").append(entry.getValue()).append("\n"));
        report.append("|===\n");

        String filename = "report_" + name + ".adoc";
        writeReportToFile(report.toString(), name);
    }
}
