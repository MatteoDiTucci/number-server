package com.ditucci.numberserver;

import java.util.HashSet;
import java.util.List;

public class NumberRepository {
    private HashSet<String> duplicates;
    private NumberLogger logger;

    public NumberRepository(HashSet<String> duplicates, NumberLogger logger) {
        this.duplicates = duplicates;
        this.logger = logger;
    }

    public void save(List<String> numbers) {
        numbers.stream()
                .filter(number -> !duplicates.contains(number))
                .forEach(this::persistAndLogNumber);
    }

    public int uniqueTotal() {
        return duplicates.size();
    }

    private void persistAndLogNumber(String number) {
        this.duplicates.add(number);
        logger.log(number);
    }
}
