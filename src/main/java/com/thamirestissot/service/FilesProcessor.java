package com.thamirestissot.service;

import com.google.gson.Gson;
import com.thamirestissot.dataManipulation.HandlerData;
import com.thamirestissot.model.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

@Component
public class FilesProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesProcessor.class);

    private Report report;

    @Autowired
    private ApplicationFiles files;

    @Autowired
    private FilesStoreService filesStoreService;

    @Scheduled(initialDelayString = "${application.job.scheduler.delay}", fixedRateString = "${application.job.scheduler.interval}")
    public void processDirectoryFiles() {
        try {
            if (filesStoreService.checkExistDirectories()) {
                Path pathIn = Paths.get(files.getDirectoryInput());

                Files.list(pathIn)
                        .filter(fileIsValid())
                        .forEach(this::processFile);


            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void processFile(Path path) {
        HandlerData handlerData = new HandlerData();
        Gson gson = new Gson();

        report = handlerData.processContent(handlerData.extractFileContent(path));
    }

    private Predicate<Path> fileIsValid() {
        return path -> files.isValidExtension(path) && filesStoreService.isNewFile(path);
    }

}