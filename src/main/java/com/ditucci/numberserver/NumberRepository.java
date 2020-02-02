package com.ditucci.numberserver;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberRepository {
    private AtomicInteger duplicatesTotal;
    private Set<String> uniques;
    private NumberLogger logger;

    public NumberRepository(AtomicInteger duplicatesTotal, Set<String> uniques, NumberLogger logger) {
        this.duplicatesTotal = duplicatesTotal;
        this.uniques = uniques;
        this.logger = logger;
    }

    public void save(List<String> numbers) {
        numbers.forEach(number -> {
            boolean isUnique = uniques.add(number);

            if (isUnique) {
                logger.log(number);
            } else {
                duplicatesTotal.incrementAndGet();
            }
        });
    }

    public int uniquesTotal() {
        return uniques.size();
    }

    public int duplicatesTotal() {
        return duplicatesTotal.get();
    }
}
