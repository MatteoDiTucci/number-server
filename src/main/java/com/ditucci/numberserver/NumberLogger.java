package com.ditucci.numberserver;

import org.slf4j.Logger;

import javax.inject.Singleton;

@Singleton
public class NumberLogger {
    private Logger logger;

    public NumberLogger(Logger logger) {
        this.logger = logger;
    }

    public void log(String number) {
        logger.info(number);
    }
}
