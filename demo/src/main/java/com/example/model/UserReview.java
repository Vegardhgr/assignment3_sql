package com.example.model;

import com.example.entity.Movie;
import com.example.entity.User;
import java.time.LocalDate;

public class UserReview {

    private int reviewId;
    private User user;
    private Movie movie;

    private float userRating;
    private String comment;
    private LocalDate reviewDate;
}
