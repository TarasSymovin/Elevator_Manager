package data.logger;

public class SynchronizedLoggerSubject implements LoggerSubject {

    private final LoggerSubject subject;

    public SynchronizedLoggerSubject(LoggerSubject subject) {
        this.subject = subject;
    }

    @Override
    public synchronized void writeText(String text) {
        subject.writeText(text);
    }

    @Override
    public synchronized void writeError(Throwable error) {
        subject.writeError(error);
    }
}
