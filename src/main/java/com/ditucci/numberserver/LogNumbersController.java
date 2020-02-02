package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

@Controller
public class LogNumbersController {

    private NumberLogger logger;

    public LogNumbersController(NumberLogger logger) {
        this.logger = logger;
    }

    @Post(value = "/numbers", consumes = MediaType.TEXT_PLAIN)
    public HttpResponse<Void> logNumbers(@Body String numbers) {
        logger.log(numbers);
        return HttpResponse.ok();
    }
}
