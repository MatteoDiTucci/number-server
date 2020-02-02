package com.ditucci.numberserver;

public class NumbersReport {
    private int lastExecutionUniques;
    private int lastExecutionDuplicates;
    private NumberRepository repository;
    private NumberLogger logger;

    public NumbersReport(int lastExecutionUniques, int lastExecutionDuplicates, NumberRepository repository, NumberLogger logger) {
        this.lastExecutionUniques = lastExecutionUniques;
        this.lastExecutionDuplicates = lastExecutionDuplicates;
        this.repository = repository;
        this.logger = logger;
    }

    public void display() {
        int newUniques = repository.uniquesTotal() - lastExecutionUniques;
        int newDuplicates = repository.duplicatesTotal() - lastExecutionDuplicates;
        int total = repository.uniquesTotal();

        lastExecutionUniques = newUniques;
        lastExecutionDuplicates = newDuplicates;

        logger.log(message(newUniques, newDuplicates, total));
    }

    private String message(int newUniques, int newDuplicates, int total) {
        return "Received " +
                newUniques +
                " unique numbers, " +
                newDuplicates +
                " duplicates. Unique total: " +
                total;
    }
}
