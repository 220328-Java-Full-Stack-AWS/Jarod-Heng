package com.revature.repositories;

import com.revature.models.User;
import com.revature.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO {

    private String UT_ID = "users_db";


    /**
     * Should retrieve a User from the DB with the corresponding username or an empty optional if there is no match.
     */
    public Optional<User> getByUsername(String username) {
        User model = new User();
        try {
            String SQL = "SELECT * FROM users_db WHERE username = ?";
            Connection conn = ConnectionFactory.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();


//            while (rs.next()) {
//                model.setItemId(rs.getInt("item_id"));
//                model.setTask(rs.getString("task"));
//                model.setDate(rs.getString("due"));
//                model.setCompleted(rs.getBoolean("completed"));
//                model.setUserId(rs.getInt("user_id"));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        return model;
        return Optional.empty();
    }

    /**
     * <ul>
     *     <li>Should Insert a new User record into the DB with the provided information.</li>
     *     <li>Should throw an exception if the creation is unsuccessful.</li>
     *     <li>Should return a User object with an updated ID.</li>
     * </ul>
     */
    public User create(User userToBeRegistered) {
        try {
            PreparedStatement psm;
        } catch (Exception e) {

        }
        return userToBeRegistered;
    }
}
