package com.learnjava.moviesapiclient;

import com.learnjava.moviesapiclient.domain.Movie;
import com.learnjava.moviesapiclient.domain.MovieInfo;
import com.learnjava.moviesapiclient.domain.Review;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public class MoviesClient {

    private final WebClient webClient;

    public MoviesClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Movie retrieveMovie(Long movieInfoId) {

        var movieInfo = invokeMovieInfoService(movieInfoId);
        var reviews = invokeMovieReviewService(movieInfoId);

        return new Movie(movieInfo, reviews);
    }

    private MovieInfo invokeMovieInfoService(Long movieInfoId) {

        var moviesInfoUrlPath = "/v1/movie_infos/{movieInfoId}";

        return webClient.get()
                .uri(moviesInfoUrlPath, movieInfoId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .block();
    }

    private List<Review> invokeMovieReviewService(Long movieInfoId) {

        String reviewUri = UriComponentsBuilder.fromUriString("/v1/reviews/")
                .queryParam("movieInfoId", movieInfoId)
                .buildAndExpand()
                .toString();

        return webClient.get()
                .uri(reviewUri)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();
    }
}
