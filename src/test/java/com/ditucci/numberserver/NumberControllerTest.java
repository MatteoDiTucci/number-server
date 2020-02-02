package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NumberControllerTest {

    private NumberRepository repository;
    private NumberController controller;


    @BeforeEach
    void setUp() {
        repository = mock(NumberRepository.class);
        controller = new NumberController(repository);
    }

    @Test
    void returnsOkFor9DigitNumber() {
        HttpResponse<Void> response = controller.logNumbers("123456789\n");

        assertEquals(OK, response.getStatus());
    }

    @Test
    void persistsSingleNumber() {
        String number = "123456789";

        controller.logNumbers(number.concat("\n"));

        verify(repository).save(List.of(number));
    }

    @Test
    void persistsMultipleNumbers() {
        String firstNumber = "123456789";
        String secondNumber = "098765431";
        String thirdNumber = "019283746";
        String numbers = String.join("\n", firstNumber, secondNumber, thirdNumber).concat("\n");

        controller.logNumbers(numbers);

        verify(repository).save(List.of(firstNumber, secondNumber, thirdNumber));
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
}