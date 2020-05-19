package com.thamirestissot.config;

import com.thamirestissot.service.FilesStoreService;
import com.thamirestissot.service.HandlerFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ProcessFilesOnStartup {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessFilesOnStartup.class);

    @Autowired
    private FilesStoreService filesStoreService;

    @Autowired
    private HandlerFile handlerFile;

    @PostConstruct
    public void init() {
        try {
            if (filesStoreService.checkExistDirectories() || filesStoreService.createdDirectoriesSuccessfully() ) {
                Path outPath = Paths.get(handlerFile.getDirectoryOut());

                Files.list(outPath)
                        .filter(path -> path.toString().endsWith(handlerFile.getFilenameDone()))
                        .forEach(this::done);
            }
        } catch (IOException e) {
            LOGGER.error("Error on init file store.", e);
        }
    }

    private void done(Path pathOut) {
        String filenameIn = pathOut.getFileName()
                .toString()
                .replace(handlerFile.getFilenameDone(), handlerFile.getFilenameReadonly());

        Path pathIn = Paths.get(handlerFile.getDirectoryIn(), filenameIn);
        filesStoreService.store(pathIn);
    }
}