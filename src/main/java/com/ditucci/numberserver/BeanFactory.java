package com.ditucci.numberserver;

import io.micronaut.context.annotation.Factory;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashSet;

@Factory
public class BeanFactory {

    @Singleton
    public NumberRepository buildNumberRepository() {
        NumberLogger logger = new NumberLogger(LoggerFactory.getLogger(NumberRepository.class));
        return new NumberRepository(0, new HashSet<>(), logger);
    }

    @Singleton
    public NumbersReport buildNumbersReport(NumberRepository repository) {
        NumberLogger logger = new NumberLogger(LoggerFactory.getLogger(NumbersReport.class));
        return new NumbersReport(0, 0, repository, logger);
    }
}
