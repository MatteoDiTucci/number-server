package com.ditucci.numberserver;

import javax.inject.Singleton;

@Singleton
public class Logger {
    private org.slf4j.Logger logger;

    public Logger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public void log(String number) {
        logger.info(number);
    }
}
