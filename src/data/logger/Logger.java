package data.logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {

    private final LoggerSubject subject;

    private Logger() {
        subject = new FormattedLoggerSubject(new SynchronizedLoggerSubject(new ConsoleLoggerSubject(resolveLoggerFile())));
    }

    public void log(String message) {
        subject.writeText(message);
    }

    public void logTitle(String message) {
        subject.writeText("\n" + message);
    }

    public void log(Exception error) {
        subject.writeText(error.getLocalizedMessage());
        subject.writeError(error);
    }

    private LoggerSubject resolveLoggerFile() {
        try {
            return new FileLoggerSubject(generateLogFileName());
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IllegalStateException("Cannot create logger file");
        }
    }

    private final static DateTimeFormatter logFileNameFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    private static String generateLogFileName() {
        return logFileNameFormatter.format(LocalDateTime.now()) + ".txt";
    }

    private static volatile Logger instance;

    public static Logger getInstance() {
        Logger ref = instance;
        if (ref == null) {
            synchronized (Logger.class) {
                ref = instance;
                if (ref == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
}
