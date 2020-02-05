package com.ditucci.numberserver;

public class Report {
    private int lastExecutionUniques;
    private int lastExecutionDuplicates;
    private Repository repository;
    private Logger logger;

    public Report(int lastExecutionUniques, int lastExecutionDuplicates, Repository repository, Logger logger) {
        this.lastExecutionUniques = lastExecutionUniques;
        this.lastExecutionDuplicates = lastExecutionDuplicates;
        this.repository = repository;
        this.logger = logger;
    }

    public void display() {
        int currentUniques = repository.uniquesTotal();
        int currentDuplicates = repository.duplicatesTotal();

        int newUniques = currentUniques - lastExecutionUniques;
        int newDuplicates = currentDuplicates - lastExecutionDuplicates;

        lastExecutionUniques = currentUniques;
        lastExecutionDuplicates = currentDuplicates;

        logger.log(message(newUniques, newDuplicates, currentUniques));
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
