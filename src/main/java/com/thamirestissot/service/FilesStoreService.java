package com.thamirestissot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilesStoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesStoreService.class);

    private List<String> filesDone = new ArrayList<>();

    @Autowired
    private ApplicationFiles applicationFiles;

    public void store(Path path) {
        filesDone.add(path.getFileName().toString());
    }

    public boolean isNewFile(Path path) {
        return !filesDone.contains(path.getFileName().toString());
    }

    public boolean checkExistDirectories() {
        Path pathIn = Paths.get(applicationFiles.getDirectoryInput());
        Path pathOut = Paths.get(applicationFiles.getDirectoryOutput());

        if (Files.notExists(pathIn)) {
            LOGGER.warn("Input directory does not exist!");
            return false;
        } else if (Files.notExists(pathOut)) {
            LOGGER.warn("Output directory does not exist!");
            return false;
        }

        return true;
    }
}

