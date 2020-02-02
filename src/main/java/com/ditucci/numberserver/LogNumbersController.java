package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.util.Arrays;
import java.util.HashSet;

@Controller
public class LogNumbersController {

    private HashSet<String> duplicates;
    private NumberLogger logger;

    public LogNumbersController(HashSet<String> duplicates, NumberLogger logger) {
        this.duplicates = duplicates;
        this.logger = logger;
    }

    @Post(value = "/numbers", consumes = MediaType.TEXT_PLAIN)
    public HttpResponse<Void> logNumbers(@Body String numberLines) {
        String[] numbers = numberLines.split("\n");

        Arrays.stream(numbers).forEach(this::logDeduplicatedNumber);

        return HttpResponse.ok();
    }

    private void logDeduplicatedNumber(String number) {
        if (!duplicates.contains(number)) {
            duplicates.add(number);
            logger.log(number);
        }
    }
}
