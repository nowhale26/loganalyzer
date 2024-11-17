import backend.academy.LogAnalyzer;
import backend.academy.textreports.AdocTextReport;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AdocTextReportTest {

    @Test
    public void testReport() throws IOException {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        LocalDateTime from = LocalDateTime.of(2015, 5, 23, 0, 0);
        LocalDateTime to = LocalDateTime.of(2015, 6, 4, 7, 6, 44);
        logAnalyzer.analyzeLogs(LogAnalyzerTest.NGINX_LOG, from, to, null, null);

        AdocTextReport report = new AdocTextReport(logAnalyzer.getRequestCounter(),
            logAnalyzer.getMostResources(),
            logAnalyzer.getMostCodeResponses(),
            logAnalyzer.getAverageSize(),
            logAnalyzer.getPercentile95(),
            LogAnalyzerTest.NGINX_LOG,
            from,
            to,
            logAnalyzer.getMaxSizeResponse(),
            logAnalyzer.getMinSizeResponse());

        String reportResult = report.buildReport("nginx_logs.txt");
        try (InputStream resourceAsStream = getClass().getResourceAsStream("report_nginx_logs.txt.adoc")) {
            String expectedResult = new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(reportResult).isEqualTo(expectedResult);
        }
    }
}
