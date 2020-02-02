package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogNumbersControllerTest {

    private NumberLogger logger;
    private LogNumbersController controller;


    @BeforeEach
    void setUp() {
        logger = mock(NumberLogger.class);
        controller = new LogNumbersController(logger);
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
}