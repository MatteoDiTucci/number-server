package com.ditucci.numberserver.acceptance;


import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;

import javax.inject.Singleton;

@Singleton
public class LogNumbersClient {
    private RxHttpClient client;

    public LogNumbersClient(@Client("/") RxHttpClient rxHttpClient) {
        this.client = rxHttpClient;
    }

    public HttpResponse<Void> logNumbers(String numbers) {
        HttpRequest<String> request = HttpRequest.POST("/numbers", numbers).contentType(MediaType.TEXT_PLAIN);
        return client.toBlocking().exchange(request);
    }
}