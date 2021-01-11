package data.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLoggerSubject implements LoggerSubject {

    private static final String PATH = "logs";

    private final File file;

    public FileLoggerSubject(String fileName) throws IOException {
        file = new LoggerFileCreator(PATH, fileName).create();
    }

    @Override
    public void writeText(String text) {
        try {
            FileWriter writer = new FileWriter(file, true);
            writer.append(text);
            writer.append('\n');
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void writeError(Throwable error) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter writer = new PrintWriter(fileWriter);
            error.printStackTrace(writer);
            writer.append('\n');
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
