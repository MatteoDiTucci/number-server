package com.ditucci.numberserver;

import io.micronaut.scheduling.annotation.Scheduled;

import javax.inject.Singleton;

@Singleton
public class Timer {
    private NumbersReport numbersReport;

    public Timer(NumbersReport numbersReport) {
        this.numbersReport = numbersReport;
    }

    @Scheduled(fixedRate = "${scheduled.fixed-rate}")
    void everyTenSeconds() {
        numbersReport.display();
    }
}
