package com.learnjava.moviesapiclient.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private MovieInfo movieInfo;
    private List<Review> reviewList;
}