package com.ditucci.numberserver.acceptance;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogNumbersTest {
    private static final String LOG_FILE_PATH = "src/test/resources/numbers.log";

    private EmbeddedServer server;
    private LogNumbersClient client;

    @BeforeEach
    void setUp() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server.getApplicationContext().getBean(LogNumbersClient.class);
    }

    @AfterEach
    void tearDown() {
        server.stop();
        wipeLogFile();
    }

    @Test
    void logsNumberInFile() {
        String number = "123456789";

        client.logNumbers(number);

        assertEquals(number + "\n", contentFromLogFile());
    }

    private String contentFromLogFile() {
        Path filePath = Paths.get(LOG_FILE_PATH);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return "Exception raised while trying to read the log file";
        }
    }

    private void wipeLogFile() {
        Path filePath = Paths.get(LOG_FILE_PATH);
        try {
            Files.writeString(filePath, "");
        } catch (IOException e) {
            // do nothing
        }
    }
}
