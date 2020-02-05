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
    public Repository buildRepository() {
        Logger logger = new Logger(LoggerFactory.getLogger(Repository.class));
        return new Repository(new AtomicInteger(), ConcurrentHashMap.newKeySet(), logger);
    }

    @Singleton
    public Report buildReport(Repository repository) {
        Logger logger = new Logger(LoggerFactory.getLogger(Report.class));
        return new Report(0, 0, repository, logger);
    }

    @Singleton
    public Queue buildQueue() {
        return new Queue(new LinkedBlockingQueue<>());
    }
}
