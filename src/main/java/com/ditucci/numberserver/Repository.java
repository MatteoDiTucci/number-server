package com.ditucci.numberserver;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Repository {
    private AtomicInteger duplicatesTotal;
    private Set<String> uniques;
    private Logger logger;

    public Repository(AtomicInteger duplicatesTotal, Set<String> uniques, Logger logger) {
        this.duplicatesTotal = duplicatesTotal;
        this.uniques = uniques;
        this.logger = logger;
    }

    public void save(List<String> numbers) {
        StringBuilder stringBuilder = new StringBuilder();

        numbers.forEach(number -> {
            boolean isUnique = uniques.add(number);

            if (isUnique) {
                addNumberToStringBuilder(stringBuilder, number);
            } else {
                duplicatesTotal.incrementAndGet();
            }
        });

        if (stringBuilder.length() > 0) {
            logger.log(stringBuilder.toString());
        }
    }

    public int uniquesTotal() {
        return uniques.size();
    }

    public int duplicatesTotal() {
        return duplicatesTotal.get();
    }

    private void addNumberToStringBuilder(StringBuilder stringBuilder, String number) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append("\n");
        }
        stringBuilder.append(number);
    }
}
