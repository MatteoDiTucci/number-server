package com.ditucci.numberserver.acceptance;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client
public interface LogNumbersClient {
    @Post("/numbers")
    HttpResponse<Void> logNumbers(String numberList);
}