package com.ditucci.numberserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NumberRepositoryTest {

    private HashSet<String> numbers;
    private NumberLogger logger;
    private NumberRepository repository;

    @BeforeEach
    void setUp() {
        numbers = new HashSet<>();
        logger = mock(NumberLogger.class);
        repository = new NumberRepository(numbers, logger);
    }

    @Test
    void savesNumbers() {
        String number = "123456789";

        repository.save(number);

        assertTrue(numbers.contains(number));
    }

    @Test
    void logsSingleNumber() {
        String number = "123456789";

        repository.save(number);

        verify(logger).log(number);
    }

    @Test
    void doesNotLogDuplicates() {
        numbers.add("123456789");

        repository.save("123456789");

        verifyZeroInteractions(logger);
    }

    @Test
    void logsMultipleNumbers() {
        String firstNumber = "123456789";
        String secondNumber = "098765431";
        String thirdNumber = "019283746";

        repository.save(firstNumber);
        repository.save(secondNumber);
        repository.save(thirdNumber);

        verify(logger).log(firstNumber);
        verify(logger).log(secondNumber);
        verify(logger).log(thirdNumber);
    }
}