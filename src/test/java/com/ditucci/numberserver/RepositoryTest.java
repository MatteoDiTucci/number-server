package com.ditucci.numberserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class RepositoryTest {

    private HashSet<String> uniques;
    private Logger logger;
    private Repository repository;

    @BeforeEach
    void setUp() {
        uniques = new HashSet<>();
        logger = mock(Logger.class);
        repository = new Repository(new AtomicInteger(), uniques, logger);
    }

    @Test
    void savesNumbers() {
        String number = "123456789";

        repository.save(List.of(number));

        assertTrue(uniques.contains(number));
    }

    @Test
    void logsSingleNumber() {
        String number = "123456789";

        repository.save(List.of(number));

        verify(logger).log(number);
    }

    @Test
    void doesNotLogDuplicates() {
        uniques.add("123456789");

        repository.save(List.of("123456789"));

        verifyZeroInteractions(logger);
    }

    @Test
    void logsMultipleNumbers() {
        String firstNumber = "123456789";
        String secondNumber = "098765431";
        String thirdNumber = "019283746";

        repository.save(List.of(firstNumber, secondNumber, thirdNumber));

        verify(logger).log(String.join("\n", firstNumber, secondNumber, thirdNumber));
    }

    @Test
    void returnsUniqueTotal() {
        uniques.add("123456789");
        uniques.add("098765432");

        assertEquals(2, repository.uniquesTotal());
    }

    @Test
    void returnsDuplicateTotal() {
        String number = "123456789";
        repository.save(List.of(number, number));

        assertEquals(1, repository.duplicatesTotal());
    }

    @Test
    void returnsDuplicateTotalAcrossMultipleExecutions() {
        String number = "123456789";
        repository.save(List.of(number, number));
        repository.save(List.of(number, number));

        assertEquals(3, repository.duplicatesTotal());
    }
}