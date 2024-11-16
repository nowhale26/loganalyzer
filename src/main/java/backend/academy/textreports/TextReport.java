package backend.academy.textreports;

import java.util.HashMap;
import java.util.Map;

public class TextReport {
    private final int requestCounter;
    private final Map<String, Integer> mostResources;
    private final Map<String, Integer> mostCodeResponses;
    private final double averageSize;
    private final int percentile95;

    public TextReport(
        int requestCounter,
        Map<String, Integer> mostResources,
        Map<String, Integer> mostCodeResponses,
        double averageSize,
        int percentile95
    ) {
        this.requestCounter = requestCounter;
        this.mostResources = new HashMap<>(mostResources);
        this.mostCodeResponses = new HashMap<>(mostCodeResponses);
        this.averageSize = averageSize;
        this.percentile95 = percentile95;
    }

    public void generateReport() {
    }

    ;
}
