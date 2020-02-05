package com.ditucci.numberserver;

import io.micronaut.context.annotation.Factory;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Factory
public class BeanFactory {

    @Singleton
    public NumberRepository buildNumberRepository() {
        NumberLogger logger = new NumberLogger(LoggerFactory.getLogger(NumberRepository.class));
        return new NumberRepository(new AtomicInteger(), ConcurrentHashMap.newKeySet(), logger);
    }

    @Singleton
    public NumbersReport buildNumbersReport(NumberRepository repository) {
        NumberLogger logger = new NumberLogger(LoggerFactory.getLogger(NumbersReport.class));
        return new NumbersReport(0, 0, repository, logger);
    }

    @Singleton
    public NumberQueue buildNumberQueue() {
        return new NumberQueue(new LinkedBlockingQueue<>());
    }
}
