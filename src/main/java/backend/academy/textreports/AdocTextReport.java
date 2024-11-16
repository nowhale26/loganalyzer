package backend.academy.textreports;

import java.util.Map;

public class AdocTextReport extends TextReport {

    public AdocTextReport(
        int requestCounter,
        Map<String, Integer> mostResources,
        Map<String, Integer> mostCodeResponses,
        double averageSize,
        int percentile95
    ) {
        super(requestCounter, mostResources, mostCodeResponses, averageSize, percentile95);
    }
}
