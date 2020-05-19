package com.thamirestissot.dataManipulation;

import com.thamirestissot.model.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.thamirestissot.factory.ParserFactory.processLine;

@Service
public class HandlerData {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerData.class);

    public Report processContent(String FileContent) {
        String fileLines[] = FileContent.split("\n");

        List<Object> data = new ArrayList<Object>();
        Arrays.stream(fileLines).forEach(line -> data.add(processLine(line)));

        return new Report(data);
    }

    public String extractFileContent(Path path) {
        Charset encoding = StandardCharsets.UTF_8;
        byte[] encoded = new byte[0];

        try {
            encoded = Files.readAllBytes(path);

        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }
}