package com.ditucci.numberserver.acceptance;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.client.exceptions.ReadTimeoutException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogNumbersToFileTest {
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
    void logsNumbersInFile() {
        String firstNumber = "123456789";
        String secondNumber = "098765432";
        String thirdNumber = "102938475";
        String expectedLogContent = String.join("\n", firstNumber, secondNumber, thirdNumber).concat("\n");

        client.logNumbers(numberLinesFrom(firstNumber, secondNumber, thirdNumber));

        assertEquals(expectedLogContent, contentFromLogFile());
    }

    @Test
    void logsDeduplicatedNumbers() {
        String duplicatedNumber = "123456789";
        String number = "098765432";

        client.logNumbers(duplicatedNumber.concat("\n"));
        client.logNumbers(number.concat("\n"));
        client.logNumbers(duplicatedNumber.concat("\n"));

        assertEquals(duplicatedNumber + "\n" + number + "\n", contentFromLogFile());
    }

    @Test
    void shutsDownOnTerminationCommand(){
        assertThrows(ReadTimeoutException.class,
                () -> client.logNumbers("terminate\n"));
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

    private String numberLinesFrom(String... numbers) {
        return String.join("\n", numbers).concat("\n");
    }
}
