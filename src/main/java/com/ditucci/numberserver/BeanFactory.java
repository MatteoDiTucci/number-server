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
        return new NumberRepository(new HashSet<>(), logger);
    }
}
