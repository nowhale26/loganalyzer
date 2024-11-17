package backend.academy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
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

        if (pathPattern.startsWith("http://") || pathPattern.startsWith("https://")) {
            try {
                BufferedReader reader = createReader(pathPattern);
                return Stream.of(reader);
            } catch (IOException e) {
                throw new AnalyzerException(e);
            }
        } else {
            String separator = FileSystems.getDefault().getSeparator();
            String normalizedPathPattern = pathPattern.replace("\\", separator).replace("/", separator);

            int lastSeparatorIndex = normalizedPathPattern.lastIndexOf(separator);
            String dirPath;
            String globPattern;
            if (lastSeparatorIndex >= 0) {
                dirPath = normalizedPathPattern.substring(0, lastSeparatorIndex);
                globPattern = normalizedPathPattern.substring(lastSeparatorIndex + 1);
            } else {
                dirPath = ".";
                globPattern = normalizedPathPattern;
            }

            Path dir = Paths.get(dirPath);
            String glob = globPattern;

            List<BufferedReader> readers = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, glob)) {
                for (Path entry : stream) {
                    if (Files.isRegularFile(entry)) {
                        try {
                            BufferedReader reader = Files.newBufferedReader(entry, StandardCharsets.UTF_8);
                            readers.add(reader);
                        } catch (IOException e) {
                            throw new AnalyzerException(e);
                        }
                    }
                }
            } catch (IOException e) {
                throw new AnalyzerException(e);
            }
            return readers.stream();
        }
    }

    private BufferedReader createReader(String path) throws IOException {
        URL url = URI.create(path).toURL();
        return new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
    }
}

