package com.ditucci.numberserver;

import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ditucci.numberserver.Queue.POISON_PILL;

@io.micronaut.http.annotation.Controller
public class Controller {

    private static final String TERMINATION_COMMAND = "terminate\n";
    private static final Pattern pattern = Pattern.compile("(\\d{9}\\n)+");

    @Inject
    private ApplicationContext appContext;

    private Queue queue;
    private QueueConsumers queueConsumers;

    public Controller(Queue queue, QueueConsumers queueConsumers) {
        this.queue = queue;
        this.queueConsumers = queueConsumers;
    }

    @Post(value = "/numbers", consumes = MediaType.TEXT_PLAIN)
    public HttpResponse<Void> logNumbers(@Body String numberLines) {

        if (TERMINATION_COMMAND.equals(numberLines)) {
            queue.add(POISON_PILL);
            queueConsumers.shutdownGracefully();
            appContext.stop();
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
        Matcher matcher = pattern.matcher(numberLines);

        return !matcher.matches();
    }
}
