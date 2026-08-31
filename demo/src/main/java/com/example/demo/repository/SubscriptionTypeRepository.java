package com.example.demo.repository;

import com.example.demo.model.UserSubscriptionType;
import com.example.demo.repository.interfaces.SubscriptionTypeRepositoryInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionTypeRepository
    implements SubscriptionTypeRepositoryInterface
{

    private final DataSource db;

    SubscriptionTypeRepository(DataSource db) {
        this.db = db;
    }

    /**
     * Retrieves the number of users in each subscription type from the database.
     *
     * @return a list of {@link UserSubscriptionType} objects representing the number of users in each subscription type
     * @throws RuntimeException if a database access error occurs
     */
    public List<UserSubscriptionType> getNumberOfUsersInSubscriptionType()
        throws RuntimeException {
        String sql =
            "SELECT SubscriptionType, COUNT(*) as usersInSubscription " +
            "FROM Users " +
            "GROUP BY SubscriptionType " +
            "ORDER BY usersInSubscription DESC";

        List<UserSubscriptionType> result = new ArrayList<>();
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
            throw new RuntimeException(
                "Could not get number of users in subscription type: ",
                e
            );
        }
        return result;
    }

    private UserSubscriptionType mapRow(ResultSet rs) throws SQLException {
        return new UserSubscriptionType(
            rs.getString("SubscriptionType"),
            rs.getInt("usersInSubscription")
        );
    }
}
