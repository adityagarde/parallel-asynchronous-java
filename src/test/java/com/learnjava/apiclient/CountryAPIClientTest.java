package com.learnjava.apiclient;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.learnjava.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;

class CountryAPIClientTest {

    WebClient webClient = WebClient.create("https://restcountries.com/v2");
    CountryAPIClient countryAPIClient = new CountryAPIClient(webClient);

    @Test
    void invokeGitHubJobsAPI_WithPageNo() {
        String countryName = "India";

        List<Country> countryList = countryAPIClient.invokeCountryAPIName(countryName);
        log(countryList.toString());

        assertTrue(countryList.size() > 0);
    }

}