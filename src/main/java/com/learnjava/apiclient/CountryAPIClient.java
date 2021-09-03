package com.learnjava.apiclient;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static com.learnjava.util.LoggerUtil.log;

public class CountryAPIClient {

    private WebClient webClient;

    public CountryAPIClient(WebClient webClient) {
        this.webClient = webClient;
    }

    // https://restcountries.com/v2/name/India
    public List<Country> invokeCountryAPIName(String name) {
        startTimer();
        String uri = UriComponentsBuilder.fromUriString("/")
                .pathSegment("name", name).path("/")
                .buildAndExpand()
                .toUriString();

        log("uri == " + uri);

        List<Country> countryList = webClient.get().uri(uri)
                .retrieve()
                .bodyToFlux(Country.class)
                .collectList()
                .block();

        timeTaken();

        return countryList;
    }

}
