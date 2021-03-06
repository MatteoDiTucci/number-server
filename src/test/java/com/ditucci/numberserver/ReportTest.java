package com.ditucci.numberserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportTest {

    private int lastExecutionUniques;
    private int lastExecutionDuplicates;
    private Repository repository;
    private Logger logger;
    private Report report;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(Repository.class);
        logger = Mockito.mock(Logger.class);
        report = new Report(0, 0, repository, logger);

        when(repository.uniquesTotal()).thenReturn(10);
        when(repository.duplicatesTotal()).thenReturn(10);
    }

    @Test
    void displaysTotalReceivedNumbers() {
        int expectedTotalReceivedNumbers = 10;

        report.display();

        verify(logger).log("Received 10 unique numbers, 10 duplicates. Unique total: " + expectedTotalReceivedNumbers);
    }

    @Test
    void displaysUniquesDeltaFromLastExecution() {
        lastExecutionUniques = 2;
        report = new Report(lastExecutionUniques, lastExecutionDuplicates, repository, logger);
        int expectedUniquesDelta = 8;

        report.display();

        verify(logger).log("Received " + expectedUniquesDelta + " unique numbers, 10 duplicates. Unique total: 10");
    }

    @Test
    void displaysDuplicatesDeltaFromLastExecution() {
        lastExecutionDuplicates = 3;
        report = new Report(lastExecutionUniques, lastExecutionDuplicates, repository, logger);
        int expectedDuplicatesDelta = 7;

        report.display();

        verify(logger).log("Received 10 unique numbers, " + expectedDuplicatesDelta + " duplicates. Unique total: 10");
    }

    @Test
    void storeCumulativeLastExecutionsUniquesAndDuplicates() {
        report = new Report(lastExecutionUniques, lastExecutionDuplicates, repository, logger);

        report.display();
        report.display();

        verify(logger).log("Received 10 unique numbers, 10 duplicates. Unique total: 10");
        verify(logger).log("Received 0 unique numbers, 0 duplicates. Unique total: 10");
    }
}
