package com.ditucci.numberserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class NumberLogger {
    private final Logger logger = LoggerFactory.getLogger(NumberLogger.class);

    public void log(String number) {
        logger.info(number);
    }
}
