package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ditucci.numberserver.NumberQueue.POISON_PILL;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NumberControllerTest {

    private NumberQueue queue;
    private NumberQueueConsumers queueConsumers;
    private NumberController controller;


    @BeforeEach
    void setUp() {
        queue = mock(NumberQueue.class);
        queueConsumers = mock(NumberQueueConsumers.class);
        controller = new NumberController(queue, queueConsumers);
    }

    @Test
    void returnsOkFor9DigitNumber() {
        String number = "123456789";
        when(queue.add(List.of(number))).thenReturn(true);

        HttpResponse<Void> response = controller.logNumbers(number + "\n");

        assertEquals(OK, response.getStatus());
    }

    @Test
    void persistsSingleNumber() {
        String number = "123456789";

        controller.logNumbers(number.concat("\n"));

        verify(queue).add(List.of(number));
    }

    @Test
    void persistsMultipleNumbers() {
        String firstNumber = "123456789";
        String secondNumber = "098765431";
        String thirdNumber = "019283746";
        String numbers = String.join("\n", firstNumber, secondNumber, thirdNumber).concat("\n");

        controller.logNumbers(numbers);

        verify(queue).add(List.of(firstNumber, secondNumber, thirdNumber));
    }

    @Test
    void returnsNotFoundForNumbersShorterThan9Digits() {
        HttpResponse<Void> response = controller.logNumbers("123");

        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    void returnsNotFoundForNumbersSeparatedBySpace() {
        HttpResponse<Void> response = controller.logNumbers("123456789 098765432");

        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    void returnsNotFoundForNumbersTerminatingWithoutNewLine() {
        HttpResponse<Void> response = controller.logNumbers("123456789\n098765432");

        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    void returnsNotFoundForAlphaNumericSLines() {
        HttpResponse<Void> response = controller.logNumbers("12345a678\n");

        assertEquals(BAD_REQUEST, response.getStatus());
    }

    @Test
    void returnsServerErrorWhenQueueDoesNotAcceptItems() {
        String number = "123456789";
        when(queue.add(List.of(number))).thenReturn(false);

        HttpResponse<Void> response = controller.logNumbers(number + "\n");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
    }

    @Test
    void passPoisonPillToQueueConsumersWhenReceivingTerminationCommand() {
        assertThrows(Exception.class,
                () -> controller.logNumbers("terminate\n"));

        verify(queue).add(POISON_PILL);
    }
}