package com.thamirestissot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class ApplicationFiles {

    @Value("$application.directory.input")
    private String directoryInput;

    @Value("$application.directory.output")
    private String directoryOutput;

    @Value("$application.filename.toprocess")
    private String filenameToprocess;

    @Value("$application.filename.processed")
    private String filenameProcessed;

    public String getDirectoryInput() {
        return directoryInput;
    }

    public String getDirectoryOutput() {
        return directoryOutput;
    }

    public String getFilenameToprocess() {
        return filenameToprocess;
    }

    public String getFilenameProcessed() {
        return filenameProcessed;
    }

    public boolean isValidExtension(Path path) {
        return path.getFileName().toString().endsWith(filenameToprocess);
    }
}
