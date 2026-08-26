package com.example.repository.interfaces;

import com.example.entity.User;
import com.example.model.MoviesMostPopular;
import com.example.model.MoviesMostWatched;
import com.example.model.UserSubscriptionType;
import java.util.List;

public interface UserRepositoryInterface {
    public List<User> getAllUsers();
    public User getUserById(int id);
    public List<User> getUsersByName(String name);
    public List<User> getUsersPage(int limit, int offset);
    public List<UserSubscriptionType> getNumberOfUsersInSubscriptionType();
    public List<MoviesMostWatched> getMostWatchedMovies();

    public boolean addUser(
        String fn,
        String ln,
        String em,
        String pw,
        String st
    );

    public boolean updateUserFirstNameLastName(
        String email,
        String fn,
        String ln
    );

    public List<MoviesMostPopular> getMostWatchedGenreByUser(int userId);
}
