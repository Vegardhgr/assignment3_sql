package com.example.repository;

import com.example.entity.User;
import com.example.model.MoviesMostPopular;
import com.example.model.MoviesMostWatched;
import com.example.model.UserSubscriptionType;
import com.example.repository.interfaces.UserRepositoryInterface;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements UserRepositoryInterface {

    private final JdbcTemplate db;

    UserRepository(JdbcTemplate db) {
        this.db = db;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM Users";
        return db.query(sql, (rs, rowNum) ->
            new User(
                rs.getInt("UserId"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Email"),
                rs.getString("Password"),
                rs.getString("SubscriptionType"),
                rs.getDate("DateJoined").toLocalDate()
            )
        );
    }

    public User getUserById(int id) {
        String sql = "SELECT * FROM Users WHERE UserId = ?";
        return db.queryForObject(
            sql,
            (rs, rowNum) ->
                new User(
                    rs.getInt("UserId"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Email"),
                    rs.getString("Password"),
                    rs.getString("SubscriptionType"),
                    rs.getDate("DateJoined").toLocalDate()
                ),
            id
        );
    }

    public List<User> getUsersByName(String name) {
        String sql = "SELECT * FROM Users WHERE FirstName LIKE ?";
        return db.query(
            sql,
            (rs, rowNum) ->
                new User(
                    rs.getInt("UserId"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Email"),
                    rs.getString("Password"),
                    rs.getString("SubscriptionType"),
                    rs.getDate("DateJoined").toLocalDate()
                ),
            "%" + name + "%"
        );
    }

    public List<User> getUsersPage(int limit, int offset) {
        String sql = "SELECT * FROM Users LIMIT ? OFFSET ?";
        return db.query(
            sql,
            (rs, rowNum) ->
                new User(
                    rs.getInt("UserId"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("Email"),
                    rs.getString("Password"),
                    rs.getString("SubscriptionType"),
                    rs.getDate("DateJoined").toLocalDate()
                ),
            limit,
            offset
        );
    }

    public List<UserSubscriptionType> getNumberOfUsersInSubscriptionType() {
        String sql =
            "SELECT SubscriptionType, COUNT(*) as usersInSubscription " +
            "FROM Users " +
            "GROUP BY SubscriptionType " +
            "ORDER BY usersInSubscription DESC";

        return db.query(sql, (rs, rowNum) ->
            new UserSubscriptionType(
                rs.getString("SubscriptionType"),
                rs.getInt("usersInSubscription")
            )
        );
    }

    public List<MoviesMostWatched> getMostWatchedMovies() {
        String sql =
            "SELECT Movies.Title as movieTitle, COUNT(*) as watchCount " +
            "FROM WatchHistory " +
            "INNER JOIN Movies ON WatchHistory.MovieId = Movies.MovieId " +
            "GROUP BY WatchHistory.MovieId, Movies.Title " +
            "ORDER BY watchCount DESC";

        return db.query(sql, (rs, rowNum) ->
            new MoviesMostWatched(
                rs.getString("movieTitle"),
                rs.getInt("watchCount")
            )
        );
    }

    public List<MoviesMostPopular> getMostWatchedGenreByUser(int userId) {
        String sql =
            "SELECT Movies.Title as movieTitle, Movies.Genre as movieGenre " +
            "FROM WatchHistory " +
            "INNER JOIN Movies ON WatchHistory.MovieId = Movies.MovieId " +
            "WHERE WatchHistory.UserId = ? " +
            "AND Movies.Genre = " +
            "(SELECT Movies.Genre " +
            "FROM WatchHistory " +
            "INNER JOIN Movies ON WatchHistory.MovieId = Movies.MovieId " +
            "WHERE WatchHistory.UserId = ? " +
            "GROUP BY Movies.Genre " +
            "ORDER BY COUNT(*) DESC LIMIT 1)";

        return db.query(
            sql,
            (rs, rowNum) ->
                new MoviesMostPopular(
                    rs.getString("movieTitle"),
                    rs.getString("movieGenre")
                ),
            userId,
            userId
        );
    }

    public boolean addUser(
        String fn,
        String ln,
        String em,
        String pw,
        String st
    ) {
        String sql =
            "INSERT INTO Users (FirstName, LastName, Email, Password, SubscriptionType) VALUES (?,?,?,?,?)";

        return db.update(sql, fn, ln, em, pw, st) > 0;
    }

    public boolean updateUserFirstNameLastName(
        String email,
        String fn,
        String ln
    ) {
        String sql =
            "UPDATE Users SET FirstName = ?, LastName = ? WHERE Email = ?";

        return db.update(sql, fn, ln, email) > 0;
    }
}
