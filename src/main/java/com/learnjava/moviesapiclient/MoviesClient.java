package com.learnjava.moviesapiclient;

import com.learnjava.moviesapiclient.domain.Movie;
import com.learnjava.moviesapiclient.domain.MovieInfo;
import com.learnjava.moviesapiclient.domain.Review;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    public List<Movie> retrieveMovieList(List<Long> movieInfoIds) {

        return movieInfoIds.stream()
                .map(item -> retrieveMovie(item))
                .collect(Collectors.toList());
    }

    public CompletableFuture<Movie> retrieveMovieWithCompletableFuture(Long movieInfoId) {

        var movieInfo = CompletableFuture.supplyAsync(() -> invokeMovieInfoService(movieInfoId));
        var reviews = CompletableFuture.supplyAsync(() -> invokeMovieReviewService(movieInfoId));

        return movieInfo
                .thenCombine(reviews, (movieInfo1, reviews1) -> {
                    return new Movie(movieInfo1, reviews1);
                });
    }

    public List<Movie> retrieveMovieListWithCompletableFuture(List<Long> movieInfoIds) {

        var movieFutures = movieInfoIds
                .stream()
                .map(item -> retrieveMovieWithCompletableFuture(item))
                .collect(Collectors.toList());

        return movieFutures
                .stream()
                .map(item -> item.join())
                .collect(Collectors.toList());

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
