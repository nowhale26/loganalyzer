import backend.academy.LogAnalyzer;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

public class LogAnalyzerTest {
    public static final String NGINX_LOG = "logs/nginx_logs.txt";
    public static final String NGINX_LARGE_LOG = "logs/nginx_large_logs.txt";

    @Test
    public void testParseLog() {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        logAnalyzer.analyzeLogs(NGINX_LOG, null, null, null, null);
        assertThat(logAnalyzer.getRequestCounter()).isEqualTo(18);
        assertThat(logAnalyzer.getMostResources().size()).isEqualTo(3);
        assertThat(logAnalyzer.getMostCodeResponses().size()).isEqualTo(3);
        assertThat(logAnalyzer.getAverageSize()).isEqualTo(423);
        assertThat(logAnalyzer.getPercentile95()).isEqualTo(3316);
        assertThat(logAnalyzer.getMaxSizeResponse()).isEqualTo(3316);
        assertThat(logAnalyzer.getMinSizeResponse()).isEqualTo(0);
    }

    @Test
    public void testParseLargeLog() {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        logAnalyzer.analyzeLogs(NGINX_LARGE_LOG, null, null, null, null);
        assertThat(logAnalyzer.getRequestCounter()).isEqualTo(51462);
        assertThat(logAnalyzer.getMostResources().size()).isEqualTo(3);
        assertThat(logAnalyzer.getMostCodeResponses().size()).isEqualTo(6);
    }

    @Test
    public void testParseLogFrom() {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        LocalDateTime from = LocalDateTime.of(2015, 5, 23, 0, 0);
        logAnalyzer.analyzeLogs(NGINX_LOG, from, null, null, null);
        assertThat(logAnalyzer.getRequestCounter()).isEqualTo(12);
        assertThat(logAnalyzer.getMostResources().size()).isEqualTo(3);
        assertThat(logAnalyzer.getMostCodeResponses().size()).isEqualTo(3);
        assertThat(logAnalyzer.getAverageSize()).isEqualTo(525);
        assertThat(logAnalyzer.getPercentile95()).isEqualTo(3316);
        assertThat(logAnalyzer.getMaxSizeResponse()).isEqualTo(3316);
        assertThat(logAnalyzer.getMinSizeResponse()).isEqualTo(0);
    }

    @Test
    public void testParseLogTo() {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        LocalDateTime to = LocalDateTime.of(2015, 6, 4, 7, 6, 44);
        logAnalyzer.analyzeLogs(NGINX_LOG, null, to, null, null);
        assertThat(logAnalyzer.getRequestCounter()).isEqualTo(16);
        assertThat(logAnalyzer.getMostResources().size()).isEqualTo(2);
        assertThat(logAnalyzer.getMostCodeResponses().size()).isEqualTo(3);
        assertThat(logAnalyzer.getAverageSize()).isEqualTo(248);
        assertThat(logAnalyzer.getPercentile95()).isEqualTo(490);
        assertThat(logAnalyzer.getMaxSizeResponse()).isEqualTo(490);
        assertThat(logAnalyzer.getMinSizeResponse()).isEqualTo(0);
    }

    @Test
    public void testParseLogFromTo() {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        LocalDateTime from = LocalDateTime.of(2015, 5, 23, 0, 0);
        LocalDateTime to = LocalDateTime.of(2015, 6, 4, 7, 6, 44);
        logAnalyzer.analyzeLogs(NGINX_LOG, from, to, null, null);
        assertThat(logAnalyzer.getRequestCounter()).isEqualTo(10);
        assertThat(logAnalyzer.getMostResources().size()).isEqualTo(2);
        assertThat(logAnalyzer.getMostCodeResponses().size()).isEqualTo(3);
        assertThat(logAnalyzer.getAverageSize()).isEqualTo(266);
        assertThat(logAnalyzer.getPercentile95()).isEqualTo(490);
        assertThat(logAnalyzer.getMaxSizeResponse()).isEqualTo(490);
        assertThat(logAnalyzer.getMinSizeResponse()).isEqualTo(0);
    }

    @Test
    public void testParseLogMethodFilter() {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        logAnalyzer.analyzeLogs(NGINX_LOG, null, null, "method", "POST");
        assertThat(logAnalyzer.getRequestCounter()).isEqualTo(2);
    }

    @Test
    public void testParseLogAgentFilter() {
        LogAnalyzer logAnalyzer = new LogAnalyzer();
        logAnalyzer.analyzeLogs(NGINX_LOG, null, null, "agent", "Mozilla*");
        assertThat(logAnalyzer.getRequestCounter()).isEqualTo(1);
    }
}
