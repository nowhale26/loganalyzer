package backend.academy;

public class AnalyzerException extends RuntimeException {
    public AnalyzerException() {
    }

    public AnalyzerException(String message) {
        super(message);
    }

    public AnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }

    public AnalyzerException(Throwable cause) {
        super(cause);
    }

    protected AnalyzerException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
