package FluxAndMono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WhatIsFlux {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static CompletableFuture<HttpResponse<String>> asyncHttpRequest() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ya.ru"))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        asyncHttpRequest().get();
    }
}
