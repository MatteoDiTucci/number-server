package com.ditucci.numberserver;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class NumberController {

    private HashSet<String> duplicates;
    private NumberLogger logger;

    public NumberController(HashSet<String> duplicates, NumberLogger logger) {
        this.duplicates = duplicates;
        this.logger = logger;
    }

    @Post(value = "/numbers", consumes = MediaType.TEXT_PLAIN)
    public HttpResponse<Void> logNumbers(@Body String numberLines) {

        if (isNotValidNumbers(numberLines)){
            return HttpResponse.badRequest();
        }

        String[] numbers = numberLines.split("\n");
        Arrays.stream(numbers).forEach(this::logDeduplicatedNumber);

        return HttpResponse.ok();
    }

    private boolean isNotValidNumbers(String numberLines) {
        Pattern pattern = Pattern.compile("(\\d{9}\\n)+");
        Matcher matcher = pattern.matcher(numberLines);

        return !matcher.matches();
    }

    private void logDeduplicatedNumber(String number) {
        if (!duplicates.contains(number)) {
            duplicates.add(number);
            logger.log(number);
        }
    }
}