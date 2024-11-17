package backend.academy;

import backend.academy.textreports.AdocTextReport;
import backend.academy.textreports.MarkdownTextReport;
import backend.academy.textreports.TextReport;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("UncommentedMain")
public class Analyzer {
    /**    Запускать приложение через configuration в IDEA, там же нужно прописать аргументы для программы
     *    Пример запуска:
     *    --path https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs --filter-field method --filter-value "HEAD" --format markdown
     */
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser();
        argsParser.initializeArguments(args);
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        logAnalyzer.analyzeLogs(argsParser.getPath(), argsParser.getFrom(), argsParser.getTo(),
            argsParser.getFilterField(), argsParser.getFilterValue());
        TextReport report;
        if ("adoc".equals(argsParser.getFormat())) {
            report = new AdocTextReport(logAnalyzer.getRequestCounter(),
                logAnalyzer.getMostResources(),
                logAnalyzer.getMostCodeResponses(),
                logAnalyzer.getAverageSize(),
                logAnalyzer.getPercentile95(),
                argsParser.getPath(),
                argsParser.getFrom(),
                argsParser.getTo(),
                logAnalyzer.getMaxSizeResponse(),
                logAnalyzer.getMinSizeResponse());
        } else {
            report = new MarkdownTextReport(logAnalyzer.getRequestCounter(),
                logAnalyzer.getMostResources(),
                logAnalyzer.getMostCodeResponses(),
                logAnalyzer.getAverageSize(),
                logAnalyzer.getPercentile95(),
                argsParser.getPath(),
                argsParser.getFrom(),
                argsParser.getTo(),
                logAnalyzer.getMaxSizeResponse(),
                logAnalyzer.getMinSizeResponse());
        }
        report.generateReport();

    }
}
