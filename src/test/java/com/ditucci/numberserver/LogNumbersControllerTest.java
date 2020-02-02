package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LogNumbersControllerTest {

    private HashSet<String> duplicates;
    private NumberLogger logger;
    private LogNumbersController controller;


    @BeforeEach
    void setUp() {
        duplicates = new HashSet<>();
        logger = mock(NumberLogger.class);
        controller = new LogNumbersController(duplicates, logger);
    }

    @Test
    void returns200For9DigitNumber() {
        HttpResponse<Void> response = controller.logNumbers("123456789");

        assertEquals(OK, response.getStatus());
    }

    @Test
    void logsNumber() {
        String number = "123456789";

        controller.logNumbers(number);

        verify(logger).log(number);
    }

    @Test
    void doesNotLogDuplicates() {
        String number = "123456789";
        duplicates.add(number);

        controller.logNumbers(number);

        verifyZeroInteractions(logger);
    }
}