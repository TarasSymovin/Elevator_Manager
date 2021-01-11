package data.logger;

import java.io.File;
import java.io.IOException;

public class LoggerFileCreator {

    private final String directoryPath;
    private final String fileName;

    public LoggerFileCreator(String directory, String fileName) {
        this.directoryPath = directory;
        this.fileName = fileName;
    }

    File create() throws IOException {
        File directory = resolveDirectory();
        return resolveFile(directory);
    }

    private File resolveDirectory() throws IOException {
        File directory = new File(directoryPath);
        createDirOrThrow(directory);
        return directory;
    }

    private File resolveFile(File directory) throws IOException {
        File file = new File(directory, fileName);
        createFileOrThrow(file);
        return file;
    }

    private void createFileOrThrow(File file) throws IOException {
        createDirOrThrow(file.getParentFile());
        if (!file.exists()) {
            boolean isCreated = file.createNewFile();
            if (!isCreated) {
                throw new IOException("Cannot create logger file with path " + file.getPath());
            }
        }
    }

    private void createDirOrThrow(File file) throws IOException {
        if (!file.exists()) {
            boolean isCreated = file.mkdir();
            if (!isCreated) {
                throw new IOException("Cannot create logger directory with path " + file.getPath());
            }
        }
    }
}
