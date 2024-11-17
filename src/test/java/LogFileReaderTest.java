import backend.academy.LogFileReader;
import java.io.BufferedReader;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LogFileReaderTest {

    @Test
    public void testUrlFile() {
        LogFileReader logFileReader = new LogFileReader();
        try (Stream<BufferedReader> files = logFileReader.getFiles(
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs")) {
            assertThat(files).isNotEmpty().allSatisfy(item -> {
                    assertThat(item.readLine()).isNotEmpty();
                }
            );
        }
    }

    @Test
    public void testLocalFile() {
        LogFileReader logFileReader = new LogFileReader();
        try (Stream<BufferedReader> files = logFileReader.getFiles("logs/nginx_logs.txt")) {
            assertThat(files).isNotEmpty().allSatisfy(item -> {
                    assertThat(item.readLine()).isNotEmpty();
                }
            );
        }
    }

    @Test
    public void testLocalFiles() {
        LogFileReader logFileReader = new LogFileReader();
        try (Stream<BufferedReader> files = logFileReader.getFiles("logs/*")) {
            assertThat(files).isNotEmpty().allSatisfy(item -> {
                    assertThat(item.readLine()).isNotEmpty();
                }
            );
        }
    }
}
