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
import java.util.Collections;
import java.util.function.Predicate;

@Component
public class FilesProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilesProcessor.class);

    @Autowired
    private HandlerFile handlerFile;

    @Autowired
    private FilesStoreService filesStoreService;

    @Scheduled(initialDelayString = "${app.job.scheduler.delay}", fixedRateString = "${app.job.scheduler.interval}")
    public void startProcessFiles() {

        try {
            if (filesStoreService.checkExistDirectories() || filesStoreService.createdDirectoriesSuccessfully()) {
                Path pathIn = Paths.get(handlerFile.getDirectoryIn());

                Files.list(pathIn)
                        .filter(fileIsValidAndNew())
                        .forEach(this::processFile);
            }
        } catch (IOException e) {
            LOGGER.error("Error on start process files.", e);
        }
    }

    private Predicate<Path> fileIsValidAndNew() {
        return path -> handlerFile.isValidFile(path) && filesStoreService.isNewFile(path);
    }

    private void processFile(Path path) {
        HandlerData handlerData = new HandlerData();

        try {
            Report report = handlerData.processContent(handlerData.extractFileContent(path));

            processStore(path, report);
        } catch (IOException e) {
            LOGGER.error("Error on process file.", e);
        }
    }

    private void processStore(Path path, Report report) throws IOException {
        Gson gson = new Gson();
        String filenameDone = handlerFile.getFilenameDone(path);

        Path pathOut = Paths.get(handlerFile.getDirectoryOut(), filenameDone);
        Files.write(pathOut, Collections.singleton(gson.toJson(report)));

        filesStoreService.store(path);
    }
}
