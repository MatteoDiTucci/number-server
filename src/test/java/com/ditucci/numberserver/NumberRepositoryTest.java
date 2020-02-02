package com.ditucci.numberserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NumberRepositoryTest {

    private HashSet<String> duplicates;
    private NumberLogger logger;
    private NumberRepository repository;

    @BeforeEach
    void setUp() {
        duplicates = new HashSet<>();
        logger = mock(NumberLogger.class);
        repository = new NumberRepository(duplicates, logger);
    }

    @Test
    void savesNumbers() {
        String number = "123456789";

        repository.save(List.of(number));

        assertTrue(duplicates.contains(number));
    }

    @Test
    void logsSingleNumber() {
        String number = "123456789";

        repository.save(List.of(number));

        verify(logger).log(number);
    }

    @Test
    void doesNotLogDuplicates() {
        duplicates.add("123456789");

        repository.save(List.of("123456789"));

        verifyZeroInteractions(logger);
    }

    @Test
    void logsMultipleNumbers() {
        String firstNumber = "123456789";
        String secondNumber = "098765431";
        String thirdNumber = "019283746";

        repository.save(List.of(firstNumber, secondNumber, thirdNumber));

        verify(logger).log(firstNumber);
        verify(logger).log(secondNumber);
        verify(logger).log(thirdNumber);
    }

    @Test
    void returnsUniqueTotal() {
        duplicates.add("123456789");
        duplicates.add("098765432");

        assertEquals(2, repository.uniqueTotal());
    }
}