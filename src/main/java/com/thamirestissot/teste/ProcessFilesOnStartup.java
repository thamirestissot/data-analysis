package com.thamirestissot.teste;

import com.thamirestissot.service.ApplicationFiles;
import com.thamirestissot.service.FilesStoreService;
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

    private static final Logger log = LoggerFactory.getLogger(ProcessFilesOnStartup.class);

    @Autowired
    private FilesStoreService filesStoreService;

    @Autowired
    private ApplicationFiles applicationFiles;

    @PostConstruct
    public void init(){
        try {
            if (filesStoreService.checkExistDirectories()) {
                Path outPath = Paths.get(applicationFiles.getDirectoryOutput());

                Files.list(outPath)
                        .filter(path -> path.toString().endsWith(applicationFiles.getFilenameProcessed()))
                        .forEach(this::done);
            }
        } catch (IOException ex) {
            log.error("Error on init file store", ex);
        }
    }

    private void done(Path pathOut) {
        String filenameIn = pathOut.getFileName()
                .toString()
                .replace(applicationFiles.getFilenameProcessed(), applicationFiles.getFilenameToprocess());

        Path pathIn = Paths.get(applicationFiles.getDirectoryInput(), filenameIn);
        filesStoreService.store(pathIn);
    }
}
