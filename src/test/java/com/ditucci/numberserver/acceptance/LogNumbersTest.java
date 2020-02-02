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
    }

    @Test
    @Disabled
    void logsNumberInFile() {
        String number = "123456789";

        client.logNumbers(number);

        assertEquals(number, contentFromLogFile());
    }

    private String contentFromLogFile() {
        Path filePath = Paths.get("src/test/resources/numbers.log");
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return "Exception raised while trying to read the log file";
        }
    }
}
