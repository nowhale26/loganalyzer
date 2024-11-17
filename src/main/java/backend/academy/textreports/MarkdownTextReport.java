package backend.academy.textreports;

import java.time.LocalDateTime;
import java.util.Map;

public class MarkdownTextReport extends TextReport {
    public MarkdownTextReport(
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
        report.append("#### Общая информация\n");
        report.append("\n\n");
        report.append("| Метрика | Значение |\n");
        report.append("|--------|------------|\n");
        report.append("| Файл | " + name + " |\n");
        report.append("| Начальная дата | ").append(from == null ? "- |\n" : from + " |\n");
        report.append("| Конечная дата | ").append(to == null ? "- |\n" : to + " |\n");
        report.append("| Количество запросов | " + requestCounter + " |\n");
        report.append("| Средний размер ответа | " + averageSize + "b |\n");
        report.append("| Наибольший размер ответа | " + maxSizeResponse + "b |\n");
        report.append("| Наименьший размер ответа | " + minSizeResponse + "b |\n");
        report.append("|95p размер ответа | " + percentile95 + "b |\n");
        report.append("\n");
        report.append("#### Наиболее часто запрашиваемые ресурсы\n");
        report.append("\n");
        report.append("| Ресурс | Количество |\n");
        report.append("|--------|------------|\n");
        mostResources.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(3)
            .forEach(entry -> report.append("| ").append(entry.getKey())
                .append(" | ").append(entry.getValue()).append(" |\n"));
        report.append("\n#### Наиболее часто встречающиеся коды ответа\n\n");
        report.append("| Код ответа | Количество |\n");
        report.append("|------------|------------|\n");
        mostCodeResponses.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(3)
            .forEach(entry -> report.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue())
                .append(" |\n"));

        String filename = "report_" + name + ".md";
        writeReportToFile(report.toString(), filename);
    }
}
