package backend.academy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class LogFileReader {

    public Stream<BufferedReader> getFiles(String pathPattern) {
        try {
            if (pathPattern.startsWith("http://") || pathPattern.startsWith("https://")) {
                List<BufferedReader> readers = new ArrayList<>();
                BufferedReader reader = createReader(pathPattern);
                readers.add(reader);
                return readers.stream();
            } else {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + pathPattern);
                Path startPath = Paths.get(pathPattern).getParent();
                if (startPath == null) {
                    startPath = Paths.get(".");
                }
                return Files.walk(startPath)
                    .filter(path -> matcher.matches(path))
                    .map(path -> {
                        try {
                            return Files.newBufferedReader(path, StandardCharsets.UTF_8);
                        } catch (IOException e) {
                            throw new AnalyzerException(e);
                        }
                    });
            }
        } catch (IOException e) {
            throw new AnalyzerException(e);
        }
    }

    private BufferedReader createReader(String path) throws IOException {
        URL url = URI.create(path).toURL();
        return new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
    }
}
