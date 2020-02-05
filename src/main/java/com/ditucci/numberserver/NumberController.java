package com.ditucci.numberserver;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class NumberController {

    private static final String TERMINATION_COMMAND = "terminate\n";
    @Inject
    private ApplicationContext appContext;

    private NumberQueue queue;
    private NumberQueueConsumers numberQueueConsumers;

    public NumberController(NumberQueue queue, NumberQueueConsumers numberQueueConsumers) {
        this.queue = queue;
        this.numberQueueConsumers = numberQueueConsumers;
    }

    public NumberController(NumberQueue queue) {
        this.queue = queue;
    }

    @Post(value = "/numbers", consumes = MediaType.TEXT_PLAIN)
    public HttpResponse<Void> logNumbers(@Body String numberLines) {

        if (TERMINATION_COMMAND.equals(numberLines)) {
            numberQueueConsumers.shutdownGracefully();
            appContext.stop();
            System.exit(0);
        }

        if (isNotValidNumbers(numberLines)) {
            return HttpResponse.badRequest();
        }

        if (addNumbersToQueue(numberLines)) {
            return HttpResponse.ok();
        }
        return HttpResponse.serverError();
    }

    private boolean addNumbersToQueue(@Body String numberLines) {
        String[] numbers = numberLines.split("\n");
        return queue.add(Arrays.asList(numbers));
    }

    private boolean isNotValidNumbers(String numberLines) {
        Pattern pattern = Pattern.compile("(\\d{9}\\n)+");
        Matcher matcher = pattern.matcher(numberLines);

        return !matcher.matches();
    }
}
