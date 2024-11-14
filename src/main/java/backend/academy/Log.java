package backend.academy;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class Log {
    private final LocalDateTime dateTime;
    private final int responseCode;
    private final int responseSize;
    private final String resource;
    private final String ip;
    private final String method;

    public Log(LocalDateTime dateTime, int responseCode, int responseSize, String resource, String ip, String method) {
        this.dateTime = dateTime;
        this.responseCode = responseCode;
        this.responseSize = responseSize;
        this.resource = resource;
        this.ip = ip;
        this.method= method;
    }
}
