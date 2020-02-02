package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

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
    public HttpResponse<Void> logNumbers(@Body String numbers) {

        if (! duplicates.contains(numbers)) {
            duplicates.add(numbers);
            logger.log(numbers);
        }

        return HttpResponse.ok();
    }
}
