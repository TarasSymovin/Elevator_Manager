package data.logger;

public class ConsoleLoggerSubject implements LoggerSubject {

    private final LoggerSubject subject;

    public ConsoleLoggerSubject(LoggerSubject subject) {
        this.subject = subject;
    }

    @Override
    public synchronized void writeText(String text) {
        System.out.println(text);
        subject.writeText(text);
    }

    @Override
    public synchronized void writeError(Throwable error) {
        error.printStackTrace();
        subject.writeError(error);
    }
}
