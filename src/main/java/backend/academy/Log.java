package backend.academy;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class Log {
    private final LocalDateTime dateTime;
    private final int responseCode;
    private final int responseSize;
    private final String resource;

    public Log(LocalDateTime dateTime, int responseCode, int responseSize, String resource) {
        this.dateTime = dateTime;
        this.responseCode = responseCode;
        this.responseSize = responseSize;
        this.resource = resource;
    }
}
