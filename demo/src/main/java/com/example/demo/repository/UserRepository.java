package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.repository.interfaces.UserRepositoryInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements UserRepositoryInterface {

    private final DataSource db;

    UserRepository(DataSource db) {
        this.db = db;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users
     * @throws RuntimeException if an error occurs while retrieving the users
     */
    public List<User> getAllUsers() throws RuntimeException {
        String sql = "SELECT * FROM Users";
        List<User> result = new ArrayList<>();

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all users: ", e);
        }

        return result;
    }

    /**
     * Retrieves a user by their ID from the database.
     * 
     * @param id the ID of the user to retrieve
     * @return an Optional containing the user if found, or empty if not found
     * @throws RuntimeException if an error occurs while retrieving the user
     */
    public Optional<User> getUserById(int id) throws RuntimeException {
        String sql = "SELECT * FROM Users WHERE UserId = ?";

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not get user by given id: ", e);
        }
        return Optional.empty();
    }

    
    /**
     * Retrieves users by their first name from the database.
     * 
     * @param name the first name of the users to retrieve
     * @return a list of users with the given first name
     * @throws RuntimeException if an error occurs while retrieving the users
     */
    public List<User> getUsersByName(String name) throws RuntimeException {
        String sql = "SELECT * FROM Users WHERE FirstName LIKE ?";
        List<User> result = new ArrayList<>();

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, name);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Could not get users by given name: ",
                e
            );
        }
        return result;
    }

    /**
     * Retrieves a page of users from the database.
     *
     * @param limit the number of users to retrieve
     * @param offset the number of users to skip
     * @return a list of users
     * @throws RuntimeException if an error occurs while retrieving the users
     */
    public List<User> getUsersPage(int limit, int offset) throws RuntimeException {
        String sql = "SELECT * FROM Users LIMIT ? OFFSET ?";
        List<User> result = new ArrayList<>();

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, limit);
            stmt.setObject(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Could not get users page with given limit and offset",
                e
            );
        }

        return result;
    }

    /**
     * Adds a new user to the database.
     *
     * @param fn the first name of the new user
     * @param ln the last name of the new user
     * @param em the email of the new user
     * @param pw the password of the new user
     * @param st the subscription type of the new user
     * @return true if the user was added successfully, false otherwise
     * @throws RuntimeException if an error occurs while adding the user
     */
    public boolean addUser(
        String fn,
        String ln,
        String em,
        String pw,
        String st
    ) throws RuntimeException {
        String sql =
            "INSERT INTO Users (FirstName, LastName, Email, Password, SubscriptionType) VALUES (?,?,?,?,?)";

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, fn);
            stmt.setString(2, ln);
            stmt.setString(3, em);
            stmt.setString(4, pw);
            stmt.setString(5, st);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Could not add new user: ", e);
        }
    }

    /**
     * Updates the first name and last name of a user in the database.
     * 
     * @param fn the new first name of the user
     * @param ln the new last name of the user
     * @param email the email of the user to update
     * @return true if the user was updated successfully, false otherwise
     * @throws RuntimeException if an error occurs while updating the user
     */
    public boolean updateUserFirstNameLastName(
        String fn,
        String ln,
        String email
    ) throws RuntimeException {
        String sql =
            "UPDATE Users SET FirstName = ?, LastName = ? WHERE Email = ?";

        try (
            Connection conn = db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, fn);
            stmt.setString(2, ln);
            stmt.setString(3, email);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(
                "Could now update user at email: " + email,
                e
            );
        }
    }

    /**
     * Maps a row from the ResultSet to a User object.
     * 
     * @param rs the ResultSet containing the user data
     * @return a User object representing the current row in the ResultSet
     * @throws SQLException if a column cannot be read from the ResultSet
     */
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("UserId"),
            rs.getString("FirstName"),
            rs.getString("LastName"),
            rs.getString("Email"),
            rs.getString("Password"),
            rs.getString("SubscriptionType"),
            rs.getDate("DateJoined").toLocalDate()
        );
    }
}
