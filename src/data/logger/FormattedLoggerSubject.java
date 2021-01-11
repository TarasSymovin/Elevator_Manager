package data.logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FormattedLoggerSubject implements LoggerSubject {

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final int LOG_TIME_LENGTH = 8;
    private static final String LOG_TAG_FORMAT = "%-" + LOG_TIME_LENGTH + "s";

    private final LoggerSubject logger;

    public FormattedLoggerSubject(LoggerSubject logger) {
        this.logger = logger;
    }

    @Override
    public void writeText(String text) {
        logger.writeText(generateTime() + "\t" + text);
    }

    @Override
    public void writeError(Throwable error) {
        logger.writeText(generateTime());
        logger.writeError(error);
    }

    private String generateTime() {
        return String.format(LOG_TAG_FORMAT, timeFormatter.format(LocalTime.now()));
    }
}
