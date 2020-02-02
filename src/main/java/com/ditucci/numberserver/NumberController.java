package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class NumberController {

    private NumberRepository repository;

    public NumberController(NumberRepository repository) {
        this.repository = repository;
    }

    @Post(value = "/numbers", consumes = MediaType.TEXT_PLAIN)
    public HttpResponse<Void> logNumbers(@Body String numberLines) {

        if (isNotValidNumbers(numberLines)) {
            return HttpResponse.badRequest();
        }

        String[] numbers = numberLines.split("\n");
        Arrays.stream(numbers).forEach(number -> repository.save(number));

        return HttpResponse.ok();
    }

    private boolean isNotValidNumbers(String numberLines) {
        Pattern pattern = Pattern.compile("(\\d{9}\\n)+");
        Matcher matcher = pattern.matcher(numberLines);

        return !matcher.matches();
    }
}
