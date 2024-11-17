package backend.academy.textreports;

import backend.academy.Constants;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.time.LocalDateTime;
import java.util.Map;

public class MarkdownTextReport extends TextReport {
    @SuppressWarnings("ParameterNumber")
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
    @SuppressWarnings("MultipleStringLiterals")
    @SuppressFBWarnings("UCPM_USE_CHARACTER_PARAMETERIZED_METHOD")
    public String buildReport(String name) {
        StringBuilder report = new StringBuilder();
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
        mostResources.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(Constants.NUMBEROFSTATS)
            .forEach(entry -> report.append("| ").append(entry.getKey())
                .append(" | ").append(entry.getValue()).append(" |\n"));
        report.append("\n#### Наиболее часто встречающиеся коды ответа\n\n");
        report.append("| Код ответа | Количество |\n");
        report.append("|------------|------------|\n");
        mostCodeResponses.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(Constants.NUMBEROFSTATS)
            .forEach(entry -> report.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue())
                .append(" |\n"));

        return report.toString();
    }

    @Override
    public void generateReport() {
        String name = extractFileName(path);
        String reportResult = buildReport(name);
        String filename = "report_" + name + ".md";
        writeReportToFile(reportResult, filename);
    }
}
