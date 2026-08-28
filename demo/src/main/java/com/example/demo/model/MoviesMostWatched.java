package com.example.demo.model;

public class MoviesMostWatched {

    private String movieTitle;
    private int watchCount;

    public MoviesMostWatched(String mt, int wc) {
        this.movieTitle = mt;
        this.watchCount = wc;
    }

    public String toString() {
        return "Title: " + movieTitle + ", Count: " + watchCount;
    }
}
