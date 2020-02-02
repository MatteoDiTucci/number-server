package com.ditucci.numberserver;

import io.micronaut.context.annotation.Factory;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

@Factory
public class BeanFactory {

    @Singleton
    public NumberRepository buildNumberRepository() {
        NumberLogger logger = new NumberLogger(LoggerFactory.getLogger(NumberRepository.class));
        return new NumberRepository(new AtomicInteger(), new HashSet<>(), logger);
    }

    @Singleton
    public NumbersReport buildNumbersReport(NumberRepository repository) {
        NumberLogger logger = new NumberLogger(LoggerFactory.getLogger(NumbersReport.class));
        return new NumbersReport(0, 0, repository, logger);
    }
}
