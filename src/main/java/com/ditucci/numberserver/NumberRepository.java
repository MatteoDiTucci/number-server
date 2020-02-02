package com.ditucci.numberserver;

import java.util.HashSet;

public class NumberRepository {
    private HashSet<String> numbers;
    private NumberLogger logger;

    public NumberRepository(HashSet<String> numbers, NumberLogger logger) {
        this.numbers = numbers;
        this.logger = logger;
    }

    public void save(String number) {
        if (numbers.contains(number)) {
            return;
        }
        numbers.add(number);
        logger.log(number);
    }
}
