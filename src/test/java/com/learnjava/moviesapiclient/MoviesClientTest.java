package com.learnjava.moviesapiclient;

import org.junit.jupiter.api.RepeatedTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.junit.jupiter.api.Assertions.*;

class MoviesClientTest {

    /*
     * We see significantly better performance when using completable future. This more profoundly seen
     * when we returned multiple movie objects (the `list` functions).
     */

    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:8080/movies/")
            .build();

    MoviesClient moviesClient = new MoviesClient(webClient);

    @RepeatedTest(10)
    void retrieveMovie() {
        startTimer();
        var movie = moviesClient.retrieveMovie(1L);
        timeTaken();

        // System.out.println("Movie Details == " + movie.toString());

        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;
    }

    @RepeatedTest(10)
    void retrieveMovieList() {
        startTimer();
        var movies = moviesClient.retrieveMovieList(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
        timeTaken();

        // System.out.println("Movie Details == " + movies.toString());

        assert movies != null;
        assert movies.size() == 7;
    }

    @RepeatedTest(10)
    void retrieveMovieWithCompletableFuture() {
        startTimer();
        var movie = moviesClient.retrieveMovieWithCompletableFuture(1L).join();
        timeTaken();

        // System.out.println("Movie Details == " + movie.toString());

        assert movie != null;
        assertEquals("Batman Begins", movie.getMovieInfo().getName());
        assert movie.getReviewList().size() == 1;
    }

    @RepeatedTest(10)
    void retrieveMovieListWithCompletableFuture() {
        startTimer();
        var movies = moviesClient.retrieveMovieListWithCompletableFuture(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
        timeTaken();

        // System.out.println("Movie Details == " + movies.toString());

        assert movies != null;
        assert movies.size() == 7;
    }
}