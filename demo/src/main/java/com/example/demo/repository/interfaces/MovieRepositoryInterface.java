package com.example.demo.repository.interfaces;

import java.util.List;

import com.example.demo.model.MoviesMostPopular;
import com.example.demo.model.MoviesMostWatched;

public interface MovieRepositoryInterface {
    public List<MoviesMostWatched> getMostWatchedMovies();
    public List<MoviesMostPopular> getMostWatchedGenreByUser(int userId);
}
