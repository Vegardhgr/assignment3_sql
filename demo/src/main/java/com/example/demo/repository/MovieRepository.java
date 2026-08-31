package com.example.demo.repository;

import com.example.demo.model.MoviesMostPopular;
import com.example.demo.model.MoviesMostWatched;
import com.example.demo.repository.interfaces.MovieRepositoryInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class MovieRepository implements MovieRepositoryInterface {

    private final DataSource db;

    MovieRepository(DataSource db) {
        this.db = db;
    }

    /**
     * Retrieves the most watched movies from the database.
     *
     * @return a list of {@link MoviesMostWatched} objects representing the most watched movies
     * @throws RuntimeException if a database access error occurs
     */
    public List<MoviesMostWatched> getMostWatchedMovies()
        throws RuntimeException {
        String sql =
            "SELECT Movies.Title as movieTitle, COUNT(*) as watchCount " +
            "FROM WatchHistory " +
            "INNER JOIN Movies ON WatchHistory.MovieId = Movies.MovieId " +
            "GROUP BY WatchHistory.MovieId, Movies.Title " +
            "ORDER BY watchCount DESC";

        List<MoviesMostWatched> result = new ArrayList<>();

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(
                        new MoviesMostWatched(
                            rs.getString("movieTitle"),
                            rs.getInt("watchCount")
                        )
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Could not get most watched movies: ",
                e
            );
        }
        return result;
    }

    /**
     * Retrieves the most watched genre by the user from the database.
     *
     * @param userId the ID of the user
     * @return a list of {@link MoviesMostPopular} objects representing the most watched genre by the user
     * @throws RuntimeException if a database access error occurs
     */
    public List<MoviesMostPopular> getMostWatchedGenreByUser(int userId)
        throws RuntimeException {
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

        List<MoviesMostPopular> result = new ArrayList<>();

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, userId);
            stmt.setObject(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(
                        new MoviesMostPopular(
                            rs.getString("movieTitle"),
                            rs.getString("movieGenre")
                        )
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Could not get most watched genre by given user; ",
                e
            );
        }
        return result;
    }
}
