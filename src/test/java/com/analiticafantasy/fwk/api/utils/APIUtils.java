package com.analiticafantasy.fwk.api.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIUtils {

    private static final HttpClient httpClient;

    static {
        httpClient = HttpClient.newBuilder().build();
    }

    public static HttpResponse<String> doGet(String url) {
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception ex) {
            throw  new RuntimeException("Error getting url");
        }
    }

}
