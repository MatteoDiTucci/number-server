package com.ditucci.numberserver;

import java.util.HashSet;
import java.util.List;

public class NumberRepository {
    private int duplicatesTotal;
    private HashSet<String> uniques;
    private NumberLogger logger;

    public NumberRepository(int duplicatesTotal, HashSet<String> uniques, NumberLogger logger) {
        this.duplicatesTotal = duplicatesTotal;
        this.uniques = uniques;
        this.logger = logger;
    }

    public void save(List<String> numbers) {
        numbers.forEach(number -> {
            if (uniques.contains(number)) {
                duplicatesTotal = duplicatesTotal + 1;
            } else {
                persistAndLogNumber(number);
            }
        });
    }

    public int uniquesTotal() {
        return uniques.size();
    }

    private void persistAndLogNumber(String number) {
        this.uniques.add(number);
        logger.log(number);
    }

    public int duplicatesTotal() {
        return duplicatesTotal;
    }
}
