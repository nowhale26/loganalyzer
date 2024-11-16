package backend.academy.textreports;

import java.util.Map;

public class MarkdownTextReport extends TextReport {
    public MarkdownTextReport(
        int requestCounter,
        Map<String, Integer> mostResources,
        Map<String, Integer> mostCodeResponses,
        double averageSize,
        int percentile95
    ) {
        super(requestCounter, mostResources, mostCodeResponses, averageSize, percentile95);
    }

    @Override
    public void generateReport() {
        super.generateReport();
    }
}
