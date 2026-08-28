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

    public List<User> getAllUsers() {
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

    public Optional<User> getUserById(int id) {
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

    public List<User> getUsersByName(String name) {
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

    public List<User> getUsersPage(int limit, int offset) {
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

    public boolean addUser(
        String fn,
        String ln,
        String em,
        String pw,
        String st
    ) {
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

    public boolean updateUserFirstNameLastName(
        String fn,
        String ln,
        String email
    ) {
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
