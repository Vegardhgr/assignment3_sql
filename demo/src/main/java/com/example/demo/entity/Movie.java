package com.example.demo.entity;

import java.time.LocalDate;

public class Movie {

    private int movieId;
    private String title;
    private String genre;
    private LocalDate releaseYear;
    private int duration;
    private float movieRating;

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public LocalDate getReleaseYear() {
        return releaseYear;
    }

    public int getDuration() {
        return duration;
    }

    public float getMovieRating() {
        return movieRating;
    }
}
