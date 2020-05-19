package com.thamirestissot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private HandlerFile handlerFile;

    public void store(Path path) {
        filesDone.add(path.getFileName().toString());
    }

    public boolean isNewFile(Path path) {
        return !filesDone.contains(path.getFileName().toString());
    }

    public boolean checkExistDirectories() {
        Path pathIn = Paths.get(handlerFile.getDirectoryIn());
        Path pathOut = Paths.get(handlerFile.getDirectoryOut());

        if (Files.notExists(pathIn) || Files.notExists(pathOut)) {
            LOGGER.debug("Both in/out directories must exists!");
            return false;
        }

        return true;
    }

    public boolean createdDirectoriesSuccessfully() {
        Path pathIn = Paths.get(handlerFile.getDirectoryIn());
        Path pathOut = Paths.get(handlerFile.getDirectoryOut());

        try {
            if (Files.notExists(pathIn))
                Files.createDirectory(pathIn);

            if (Files.notExists(pathOut))
                Files.createDirectory(pathOut);

        } catch (IOException e) {
            LOGGER.error("Error on create directory.", e);
        }

        return true;
    }
}