package com.example.model;

public class MoviesMostPopular {

    private String title;
    private String genre;

    public MoviesMostPopular(String t, String g) {
        this.title = t;
        this.genre = g;
    }

    public String toString() {
        return "Title: " + title + ", Genre: " + genre;
    }
}
