package com.ditucci.numberserver;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;

@Singleton
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

    private void persistAndLogNumber(String number) {
        this.duplicates.add(number);
        logger.log(number);
    }
}
