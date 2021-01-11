package data.logger;

public interface LoggerSubject {

    void writeText(String text);

    void writeError(Throwable error);

}
