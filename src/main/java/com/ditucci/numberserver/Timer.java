package com.ditucci.numberserver;

import io.micronaut.scheduling.annotation.Scheduled;

import javax.inject.Singleton;

@Singleton
public class Timer {
    private Report report;

    public Timer(Report report) {
        this.report = report;
    }

    @Scheduled(fixedRate = "${scheduled.fixed-rate}")
    void scheduledReport() {
        report.display();
    }
}
